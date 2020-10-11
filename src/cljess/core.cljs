(ns cljess.core
  (:require [reagent.core :as r]
            [reagent.dom :as rDOM]
            [reagent.debug :as debug]
            [cljess.piece :as piece]
            [cljess.board :as board]
            [cljess.logic :as logic]
            [cljess.random-bot :as random-bot]))

(defonce game-state (r/atom logic/new-game-state))
(defonce app-state
  (r/atom {:flip-board false
           :square-selection nil
           :debug-piece-selection nil
           :debug-mode false
           :show-new-game-dialogue false
           :player :w}))

(defn new-game!
  ([]
   (reset! game-state logic/new-game-state)
   (reset! app-state {:flip-board false
                      :square-selection nil
                      :debug-piece-selection nil
                      :debug-mode false
                      :show-new-game-dialogue false
                      :player :w}))
  ([player-color bot-choice]
   (reset! game-state logic/new-game-state)
   (reset! app-state {:flip-board (case player-color :w false :b true)
                      :square-selection nil
                      :debug-piece-selection nil
                      :debug-mode false
                      :show-new-game-dialogue false
                      :player player-color})
   (when (= player-color :b) (reset! game-state (random-bot/make-move @game-state)))))

(defn clear-board! []
  (swap! game-state (fn [state board] (assoc state :board board)) board/empty-board)
  (swap! app-state #(assoc % :square-selection nil)))



(defn square [piece coord color]
  [:button
   {:class (str "square " color)
    :on-click (fn [] (if (:debug-mode @app-state)
                       (when-not (nil? (:debug-piece-selection @app-state))
                         (swap! game-state
                                (fn [state pos piece] (assoc state :board (assoc-in (:board state) pos piece)))
                                coord (:debug-piece-selection @app-state)))
                       (if (and (nil? (:square-selection @app-state))
                                (zero? (logic/get-piece (:board @game-state) coord)))
                         nil
                         (if (nil? (:square-selection @app-state))
                           (swap! app-state #(assoc % :square-selection coord))
                           (if (logic/legal? @game-state (:square-selection @app-state) coord)
                             (do (swap! game-state logic/make-move (:square-selection @app-state) coord)
                                 (swap! app-state #(assoc % :square-selection nil))
                                 (reset! game-state (random-bot/make-move @game-state)))
                             (swap! app-state #(assoc % :square-selection nil)))))))}
   [:img {:src piece}]])

(defn reverse-board [board]
  (reverse (reduce (fn [res l] (conj res (reverse l))) [] board)))

(defn chess-board []
  (let [v ["light" "dark"]]
    #(if-not (:flip-board @app-state)
      (into [:div] (for [i (range 8)]
                     [:div {:class "board-row"}
                      (map-indexed (fn [j el]
                                     (square (piece/image el) [i j] (v (mod (+ i j) 2))))
                                   (nth (:board @game-state) i))]))
      (into [:div] (for [i (range 8)]
                     [:div {:class "board-row"}
                      (map-indexed (fn [j el]
                                     (square (piece/image el) [(- 7 i) (- 7 j)] (v (mod (+ i j) 2))))
                                   (nth (reverse-board (:board @game-state)) i))])))))

(defn piece-pane []
  (reduce (fn [res piece]
            (conj res
                  [:button {:on-click (fn [] (swap! app-state #(assoc % :debug-piece-selection piece)))}
                   [:img {:src (piece/image piece)}]]))
          [:div]
          [:wk :wq :wr :wb :wn :wp :bk :bq :br :bb :bn :bp]))

(defn turn-display []
  [:div
   [:h2 "Turn"]
   (case (:turn @game-state)
     :w [:div {:class "turn-display light"}]
     :b [:div {:class "turn-display dark"}])])

(defn result-display []
  [:div
   [:h2 (case (:result @game-state)
          :w "White wins!"
          :b "Black wins!"
          :draw "Draw")]])


(defn create-game-form []
  (let [s (r/atom {:player-color :w
                   :bot-choice "random-bot"})]
    (fn []
      [:div
       [:form {:on-submit (fn [e]
                            (.preventDefault e)
                            (debug/dbg @s)
                            (new-game! (:player-color @s) (:bot-choice @s)))}
        [:h2 "Create Game"]
        [:p1 "Play as: "]
        [:input {:type :radio :name :player :value :white :default-checked true
                 :on-click (fn [] (swap! s #(assoc % :player-color :w)))}]
        [:label {:for :white} "White"]
        [:input {:type :radio :name :player :value :black
                 :on-click (fn [] (swap! s #(assoc % :player-color :b)))}]
        [:label {:for :black} "Black"]
        [:br]
        [:br]
        [:label {:for :bots} "Play against: "]
        [:select {:name :bots
                  :on-change (fn [e] (swap! s #(assoc % :bot-choice (-> e .-target .-value))))}
         [:option {:value "random-bot"} "Random Bot"]
         [:option {:value "mcts-bot"} "Monte Carlo Tree Search Bot (Under Construction)"]
         [:option {:value "ai-bot"} "AI Bot (Under Construction)"]]
        [:br]
        [:br]
        [:input {:type :submit :value "Start Game!"}]]])))

(defn app []
  [:div
   [:h1 "Cljess!"]
   [chess-board]
   [:button {:class "medium-button"
             :on-click (fn [] (swap! app-state #(assoc % :show-new-game-dialogue true)))} "New Game"]
   [:button {:class "medium-button"
             :on-click (fn [] (swap! app-state #(assoc % :debug-mode true)))} "God Mode"]
   [:button {:class "medium-button"
             :on-click (fn [] (swap! app-state #(assoc % :debug-mode false)))} "Play Mode"]
   [:button {:class "medium-button" :on-click #(clear-board!)} "Clear Board"]
   (when (:show-new-game-dialogue @app-state) [create-game-form])
   (when-not (nil? (:result @game-state)) [result-display])
   (when (:debug-mode @app-state) [piece-pane])
   [turn-display]])

(defn init! []
  (r/render [app] (.getElementById js/document "app")))

(init!)
