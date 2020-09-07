(ns cljess.logic
  (:require [cljess.piece :as piece]
            [cljess.board :as board]))

(defn get-piece "Get the piece (e.g. :wr) at pos on board"
  [board pos] (cond (vector? pos) (get-in board pos) (keyword? pos) (let [p (board-to-coord pos)] (get-in board p))))

(defn on-board? "Determine if a position is a valid board location"
  [[y x]] (and (>= x 0) (>= y 0) (< x 8) (< y 8)))

(defn same-color? "Return true if the pieces at pos1 and pos2 on board are the same color"
  [board pos1 pos2] (= (piece/color (get-piece board pos1)) (piece/color (get-piece board pos2))))

(defn opposite-color? "Return true if the pieces at pos1 and pos2 on board are opposite colors"
  [board pos1 pos2]
  (let [piece1 (get-piece board pos1) piece2 (get-piece board pos2)]
    (or (and (= (piece/color piece1) :w) (= (piece/color piece2) :b))
        (and (= (piece/color piece1) :b) (= (piece/color piece2) :w)))))

(defn vector-add "Perform elementwise addition of two equal size vectors" [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

;;helper functions for finding legal moves
(defn moves-in-direction "Get the legal moves for a piece at pos on board in direction (e.g. :nw, :up, etc.)"
  [board pos dir]
  (let [dir-vec (case dir :up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1])]
    (loop [moves #{} next-pos (vector-add pos dir-vec)]
      (if-not (on-board? next-pos) moves
            (if (zero? (get-piece board next-pos)) (recur (conj moves next-pos) (vector-add next-pos dir-vec))
                (if (same-color? board pos next-pos) moves (conj moves next-pos)))))))

(defn moves-in-directions [board pos dirs]
  (reduce clojure.set/union (map #(moves-in-direction board pos %) dirs)))

;;helper functions for finding checks
(defn capture-in-direction [board pos dir]
  (let [dir-vec (case dir :up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1])]
    (loop [capture #{} next-pos (vector-add pos dir-vec)]
      (if-not (board/on-board? next-pos) capture
              (if (zero? (get-piece board next-pos)) (recur capture (vector-add next-pos dir-vec))
                  (if (same-color? board pos next-pos) capture (conj capture (piece/typeof (get-piece board next-pos)))))))))

(defn captures-in-directions [board pos dirs]
  (reduce clojure.set/union (map #(capture-in-direction board pos %) dirs)))

(defn knight-captures [board pos]
  (reduce (fn [res vec]
            (let [new-pos (vector-add pos vec)]
              (if (opposite-color? board pos new-pos) (conj res (piece/typeof (get-piece board new-pos))) res)))
          #{}
          [[1 2] [-1 2] [1 -2] [2 1] [-2 1] [2 -1] [-2 -1]]))

(defn pawn-captures [board pos]
  (let [moves (case (piece/color (get-piece board pos)) :w [[-1 1] [-1 -1]] :b [[1 1] [1 -1]])
        new-pos1 (vector-add pos (moves 0))
        new-pos2 (vector-add pos (moves 1))]
    (conj #{}
          (when (opposite-color? board pos new-pos1) (piece/typeof (get-piece board new-pos1)))
          (when (opposite-color? board pos new-pos2) (piece/typeof (get-piece board new-pos2))))))

(defn check? [board king-pos]
  (or (contains? (captures-in-directions board king-pos [:up :down :left :right]) :r)
      (contains? (captures-in-directions board king-pos [:nw :ne :sw :se]) :b)
      (contains? (captures-in-directions board king-pos [:up :down :left :right :nw :ne :sw :se]) :q)
      (contains? (knight-captures board king-pos) :n)
      (contains? (pawn-captures board king-pos) :p)))

;;code to find semi legal moves (i.e. without taking checks or pins into account) for a piece at pos on board
(defmulti semi-legal-moves (fn [{board :board} pos] (piece/typeof (get-piece board pos))))
(defmethod semi-legal-moves :r [{board :board turn :turn} pos]
  (if (= turn (piece/color (get-piece board pos))) (moves-in-directions board pos [:up :down :left :right]) #{}))
(defmethod semi-legal-moves :b [{board :board turn :turn} pos]
  (if (= turn (piece/color (get-piece board pos))) (moves-in-directions board pos [:nw :ne :sw :se]) #{}))
(defmethod semi-legal-moves :q [{board :board turn :turn} pos]
  (if (= turn (piece/color (get-piece board pos))) (moves-in-directions board pos [:up :down :left :right :nw :ne :sw :se]) #{}))
(defmethod semi-legal-moves :n [{board :board turn :turn} pos]
  (if (= turn (piece/color (get-piece board pos)))
    (reduce (fn [res vec]
              (let [new-pos (into [] (map + pos vec))]
                (if (and (on-board? new-pos) (not (same-color? board pos new-pos))) (conj res new-pos) res)))
            #{}
            [[1 2] [-1 2] [1 -2] [-1 -2] [2 1] [-2 1] [2 -1] [-2 -1]]) #{}))
(defmethod semi-legal-moves :p [{board :board turn :turn} [y x :as pos]]
  (let [color (piece/color (get-piece board pos))]
    (if (= color turn)
      (case color
        :w (disj (conj #{}
                       (when (zero? (get-in board [(dec y) x])) [(dec y) x])
                       (when (opposite-color? board pos [(dec y) (dec x)]) [(dec y) (dec x)])
                       (when (opposite-color? board pos [(dec y) (inc x)]) [(dec y) (inc x)])) nil)
        :b (disj (conj #{}
                       (when (zero? (get-in board [(inc y) x])) [(inc y) x])
                       (when (opposite-color? board pos [(inc y) (dec x)]) [(inc y) (dec x)])
                       (when (opposite-color? board pos [(inc y) (inc x)]) [(inc y) (inc x)])) nil)) #{})))
(defmethod semi-legal-moves :k [{board :board turn :turn} pos]
  (if (= turn (piece-color (get-piece board pos)))
    (reduce (fn [res vec]
              (let [new-pos (vector-add pos vec)]
                (if (and (on-board? new-pos) (not (same-color? board pos new-pos)) (not (check? board new-pos))) (conj res new-pos) res)))
            #{}
            [[0 1] [0 -1] [1 0] [-1 0] [1 1] [1 -1] [-1 1] [-1 -1]]) #{}))
(defmethod semi-legal-moves :none [_ _] #{})

(defn legal? [state from to] (cond (keyword? from) (contains? (semi-legal-moves state (board-to-coord from)) (board-to-coord to)) (vector? from) (contains? (semi-legal-moves state from) to)))
