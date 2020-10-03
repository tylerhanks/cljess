(ns cljess.core
  (:require [reagent.core :as r]
            [reagent.dom :as rDOM]
            [reagent.debug :as debug]
            [cljess.piece :as piece]
            [cljess.board :as board]
            [cljess.logic :as logic]))

(defonce game-state (r/atom nil))
(defonce flip-board (r/atom false))
(defonce square-selection (r/atom nil))
(defonce debug-piece-selection (r/atom nil))
(defonce debug-mode (r/atom false))
(defonce show-new-game-dialogue (r/atom false))

(defn new-game!
  ([]
   (logic/reset-game-state! game-state)
   (reset! square-selection nil)
   (reset! debug-mode false)
   (reset! debug-piece-selection nil)
   (reset! show-new-game-dialogue false))
  ([player-color bot-choice]
   (logic/reset-game-state! game-state)
   (reset! square-selection nil)
   (reset! debug-mode false)
   (reset! show-new-game-dialogue false)
   (reset! flip-board (case player-color :w false :b true))))
  ;;(reset! flip-board (case player-color "white" false "black" true))
  ;;(reset! show-new-game-dialogue false))
  ;;(reset! flip-board (case player-color :w false :b true)))

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
                                  (do (swap! game-state logic/make-move @square-selection coord)
                                      (reset! square-selection nil))
                                  (reset! square-selection nil)))))}
   [:img {:src piece}]])

(defn reverse-board [board]
  (reverse (reduce (fn [res l] (conj res (reverse l))) [] board)))

(defn chess-board []
  (let [v ["light" "dark"]]
    #(if-not @flip-board
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
            (conj res [:button {:on-click #(reset! debug-piece-selection piece)} [:img {:src (piece/image piece)}]]))
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
  #_(let [player-color (r/atom "white")
        bot-choice (r/atom "random-bot")]
    (fn []
      [:div
       [:form #_{:on-submit (fn [e]
                            (.preventDefault e)
                            #_(new-game! @player-color @bot-choice))}
        [:h2 "Create Game"]
        [:p1 "Play as: "]
        [:input {:type :radio :name :player :value :white
                 }]
        [:label {:for :white} "White"]
        [:input {:type :radio :name :player :value :black
                 }]
        [:label {:for :black} "Black"]
        [:br]
        [:br]
        [:label {:for :bots} "Play against: "]
        [:select {:name :bots
                  :on-change #(reset! bot-choice (% .-target .-value))}
         [:option {:value "random-bot"} "Random Bot"]
         [:option {:value "mcts-bot"} "Monte Carlo Tree Search Bot (Under Construction)"]
         [:option {:value "ai-bot"} "AI Bot (Under Construction)"]]
        [:br]
        [:br]
        [:input {:type :submit :value "Start Game!"}]]]))

(defn app []
  [:div
   [:h1 "Cljess!"]
   [chess-board]
   [:button {:class "medium-button" :on-click #(reset! show-new-game-dialogue true)} "New Game"]
   ;;[:button {:class "medium-button" :on-click #(reset! debug-mode true)} "God Mode"]
   ;;[:button {:class "medium-button" :on-click #(reset! debug-mode false)} "Play Mode"]
   ;;[:button {:class "medium-button" :on-click #(clear-board!)} "Clear Board"]
   (when @show-new-game-dialogue [create-game-form])
   (when-not (nil? (:result @game-state)) [result-display])
   ;;(when @debug-mode [piece-pane])
   [turn-display]])

(defn init! []
  (new-game!)
  (r/render [app] (.getElementById js/document "app")))

(init!)
