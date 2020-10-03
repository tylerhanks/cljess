;;---------------------------------------------------------;;
;;-Random Bot makes a random legal move. It is pretty dumb-;;
;;---------------------------------------------------------;;
(ns cljess.random-bot
  (:require [cljess.logic :as logic]))

(defn make-move [game-state]
  (let [moves (vec (logic/all-legal-moves game-state))]
    (logic/make-move game-state
                     (moves (rand-int (count moves))))))
