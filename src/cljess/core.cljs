(ns cljess.core
  (:require [reagent.core :as r]
            [reagent.dom :as rDOM]
            [cljess.piece :as piece]
            [cljess.board :as board]
            [cljess.logic :as logic]))

(defonce game-state (r/atom nil))

(defonce square-selection (r/atom nil))
(defonce debug-piece-selection (r/atom nil))
(defonce debug-mode (r/atom false))

(defn new-game! []
  (logic/reset-game-state! game-state)
  (reset! square-selection nil)
  (reset! debug-mode false))

(defn clear-board! []
  (swap! game-state (fn [state board] (assoc state :board board)) board/empty-board)
  (reset! square-selection nil))

(defn square [piece coord color]
  [:button
   {:class (str "square " color)
    :on-click #(if @debug-mode
                  (if-not (nil? @debug-piece-selection)
                    (swap! game-state
                           (fn [state pos piece] (assoc state :board (assoc-in (:board state) pos piece)))
                           coord @debug-piece-selection))
                  (if (and (nil? @square-selection) (zero? (logic/get-piece (:board @game-state) coord))) nil
                            (if (nil? @square-selection) (reset! square-selection coord)
                                (if (logic/legal? @game-state @square-selection coord)
                                  (do (swap! game-state (fn [state from to] (logic/make-move state from to)) @square-selection coord)
                                      (reset! square-selection nil))
                                  (reset! square-selection nil)))))}
   [:img {:src piece}]])

(defn chess-board []
  (let [v ["light" "dark"]] (into [:div] (for [i (range 8)] [:div {:class "board-row"} (map-indexed (fn [j el] (square (piece/image el) [i j] (v (mod (+ i j) 2)))) (nth (:board @game-state) i))]))))

(defn piece-pane []
  (reduce (fn [res piece]
            (conj res [:button {:on-click #(reset! debug-piece-selection piece)} [:img {:src (piece/image piece)}]]))
          [:div]
          [:wk :wq :wr :wb :wn :wp :bk :bq :br :bb :bn :bp]))

(defn turn-display []
  [:div
   [:h2 "Turn"]
   (case (:turn @game-state)
     :w [:div {:class "turn-display light"}]
     :b [:div {:class "turn-display dark"}])])

(defn app []
  [:div
   [:h1 "Cljess!"]
   [chess-board]
   [:button {:class "medium-button" :on-click #(new-game!)} "New Game"]
   [:button {:class "medium-button" :on-click #(reset! debug-mode true)} "God Mode"]
   [:button {:class "medium-button" :on-click #(reset! debug-mode false)} "Play Mode"]
   [:button {:class "medium-button" :on-click #(clear-board!)} "Clear Board"]
   (if @debug-mode [piece-pane] nil)
   [turn-display]])

(defn init! []
  (new-game!)
  (r/render [app] (.getElementById js/document "app")))

(init!)
