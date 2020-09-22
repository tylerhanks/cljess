(ns cljess.logic
  (:require [cljess.piece :as piece]
            [cljess.board :as board]
            [reagent.core :as r]))

(defn get-piece "Get the piece (e.g. :wr) at pos on board"
  [board pos] (cond (vector? pos) (get-in board pos) (keyword? pos) (let [p (board/board-to-coord pos)] (get-in board p))))

(defn get-color "Get the color of the piece at pos on board"
  [board pos] (piece/color (get-piece board pos)))

(defn on-board? "Determine if a position is a valid board location"
  [[y x]] (and (>= x 0) (>= y 0) (< x 8) (< y 8)))

(defn same-color? "Return true if the pieces at pos1 and pos2 on board are the same color"
  [board pos1 pos2] (= (get-color board pos1) (get-color board pos2)))

(defn opposite-color? "Return true if the pieces at pos1 and pos2 on board are opposite colors"
  [board pos1 pos2]
  (let [other-color {:w :b :b :w}]
    (= (get-color board pos1) (other-color (get-color board pos2)))))

(defn vector-add "Perform elementwise addition of two equal size vectors" [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(def dir-to-vec {:up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1]})

;;Single Move: [[from-y from-x] [to-y to-y]]
;;Complete Move: key: [from to] val: {:additional-action [:move single-move]-or-[:delete pos] :effect-descriptors [effect keywords e.g. :progress, :king-move, etc.]}

(defn move-piece "Returns a new board which is the same as board with the piece at from moved to to"
  ([board from to]
  (let [piece (get-piece board from)
        new-board (assoc-in board from 0)]
    (assoc-in new-board to piece)))
  ([board [from to]] (move-piece board from to)))

(defn delete-piece "Delete the piece at pos on board"
  [board pos] (assoc-in board pos 0))

;;helper functions for finding legal moves
(defn moves-in-direction "Get the legal moves for a piece at pos on board in direction (e.g. :nw, :up, etc.)"
  [board pos dir]
  (let [dir-vec (case dir :up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1])]
    (loop [moves {} next-pos (vector-add pos dir-vec)]
      (if-not (and (on-board? next-pos) (zero? (get-piece board next-pos))) moves
              (recur (assoc moves [pos next-pos] {}) (vector-add next-pos dir-vec))))))

;;helper functions for finding legal moves
#_(defn moves-in-direction "Get the legal moves for a piece at pos on board in direction (e.g. :nw, :up, etc.)"
  [board pos dir]
  (let [dir-vec (case dir :up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1])]
    (loop [moves #{} next-pos (vector-add pos dir-vec)]
      (if-not (on-board? next-pos) moves
            (if (zero? (get-piece board next-pos)) (recur (conj moves next-pos) (vector-add next-pos dir-vec))
                (if (same-color? board pos next-pos) moves (conj moves next-pos)))))))

(defn moves-in-directions [board pos dirs]
  (reduce merge (map #(moves-in-direction board pos %) dirs)))

;;helper functions for finding captures
(defn capture-in-direction [board pos dir]
  (let [dir-vec (case dir :up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1])]
    (loop [next-pos (vector-add pos dir-vec)]
      (if (or (not (on-board? next-pos)) (same-color? board pos next-pos)) {}
          (if (opposite-color? board pos next-pos) {[pos next-pos] {:effect-descriptors [:progress]}}
              (recur (vector-add next-pos dir-vec)))))))

(defn captures-in-directions [board pos dirs]
  (reduce merge (map #(capture-in-direction board pos %) dirs)))

;;helper functions for finding checks
(defn check-in-direction [board pos dir]
  (let [dir-vec (case dir :up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1])]
    (loop [capture #{} next-pos (vector-add pos dir-vec)]
      (if-not (on-board? next-pos) capture
              (if (zero? (get-piece board next-pos)) (recur capture (vector-add next-pos dir-vec))
                  (if (same-color? board pos next-pos) capture (conj capture (piece/typeof (get-piece board next-pos)))))))))

(defn checks-in-directions [board pos dirs]
  (reduce clojure.set/union (map #(check-in-direction board pos %) dirs)))

(defn pin-in-direction [board kpos dir]
  (let [dir-vec (dir-to-vec dir)
        qb #{:q :b} qr #{:q :r}
        pinners (case dir :up qr :down qr :left qr :right qr :nw qb :ne qb :sw qb :se qb)]
    (loop [pinned-piece nil next-pos (vector-add kpos dir-vec)]
      (if (or (not (on-board? next-pos))
              (and (not (nil? pinned-piece)) (same-color? board kpos next-pos))
              (and (nil? pinned-piece) (opposite-color? board kpos next-pos))
              (and (not (nil? pinned-piece)) (opposite-color? board kpos next-pos) (not (contains? pinners (piece/typeof (get-piece board next-pos))))))
        nil
        (if (and (contains? pinners (piece/typeof (get-piece board next-pos))) (opposite-color? board kpos next-pos))
          pinned-piece
          (if-not (zero? (get-piece board next-pos))
            (recur next-pos (vector-add next-pos dir-vec))
            (recur pinned-piece (vector-add next-pos dir-vec))))))))

(defn knight-checks [board pos]
  (reduce (fn [res vec]
            (let [new-pos (vector-add pos vec)]
              (if (opposite-color? board pos new-pos) (conj res (piece/typeof (get-piece board new-pos))) res)))
          #{}
          [[1 2] [-1 2] [1 -2] [2 1] [-2 1] [2 -1] [-2 -1]]))

(defn pawn-checks [board pos]
  (let [moves (case (piece/color (get-piece board pos)) :w [[-1 1] [-1 -1]] :b [[1 1] [1 -1]])
        new-pos1 (vector-add pos (moves 0))
        new-pos2 (vector-add pos (moves 1))]
    (conj #{}
          (when (opposite-color? board pos new-pos1) (piece/typeof (get-piece board new-pos1)))
          (when (opposite-color? board pos new-pos2) (piece/typeof (get-piece board new-pos2))))))

(defn check? [board king-pos]
  (or (contains? (checks-in-directions board king-pos [:up :down :left :right]) :r)
      (contains? (checks-in-directions board king-pos [:nw :ne :sw :se]) :b)
      (contains? (checks-in-directions board king-pos [:up :down :left :right :nw :ne :sw :se]) :q)
      (contains? (knight-checks board king-pos) :n)
      (contains? (pawn-checks board king-pos) :p)))

(defn king-pos [{:keys [wk-pos bk-pos]} color] (case color :w wk-pos :b bk-pos))

(defn avoid-check "Prunes a set of moves to return only those that get the current side out of check"
  [board checked-king-pos moves]
  (reduce (fn [res move]
            (if-not (check? (move-piece board (key move)) checked-king-pos)
              (conj res move) res)) {} moves))

;;code to generate all legal moves for the piece at pos on board
(defmulti legal-moves (fn [{board :board} pos] (piece/typeof (get-piece board pos))))

(defmethod legal-moves :b [{:keys [board turn check abs-pins] :as state} pos] ;;bishop moves
  (if (not= turn (get-color board pos))
    {}
    (let [bishop-dirs [:nw :ne :sw :se]
          moves (merge (moves-in-directions board pos bishop-dirs) (captures-in-directions board pos bishop-dirs))]
      (if (or check (contains? abs-pins pos))
        (avoid-check board (king-pos state turn) moves)
        moves))))

(defmethod legal-moves :q [{:keys [board turn check abs-pins] :as state} pos] ;;queen moves
  (if (not= turn (get-color board pos))
    {}
    (let [queen-dirs [:left :right :up :down :nw :ne :sw :se]
          moves (merge (moves-in-directions board pos queen-dirs) (captures-in-directions board pos queen-dirs))]
      (if (or check (contains? abs-pins pos))
        (avoid-check board (king-pos state turn) moves)
        moves))))

(defmethod legal-moves :n [{:keys [board turn check abs-pins] :as state} pos] ;;knight moves
  (if (or (not= turn (get-color board pos)) (contains? abs-pins pos))
    {}
    (let [moves (reduce (fn [res vec]
                          (let [new-pos (vector-add pos vec)]
                            (if (zero? (get-piece board new-pos))
                              (assoc res [pos new-pos] {})
                              (if (opposite-color? board pos new-pos)
                                (assoc res [pos new-pos] {:effect-descriptors [:progress]}) res))))
                        {}
                        [[1 2] [-1 2] [1 -2] [-1 -2] [2 1] [-2 1] [2 -1] [-2 -1]])]
      (if check (avoid-check board (king-pos state turn) moves) moves))))

(defmethod legal-moves :k [{board :board turn :turn}])

(defmethod legal-moves :none [_ _] {})

(defn update-check [{:keys [board wk-pos bk-pos] :as game-state}]
  (assoc game-state :check (or (check? board wk-pos) (check? board bk-pos))))

(defn update-abs-pins [{:keys [board wk-pos bk-pos] :as game-state}]
  (let [dirs [:up :down :left :right :nw :ne :sw :se]]
    (assoc game-state :abs-pins
           (disj (clojure.set/union
                  (reduce (fn [res dir] (conj res (pin-in-direction board wk-pos dir))) #{} dirs)
                  (reduce (fn [res dir] (conj res (pin-in-direction board bk-pos dir))) #{} dirs)) nil))))

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
  (if (= turn (piece/color (get-piece board pos)))
    (reduce (fn [res vec]
              (let [new-pos (vector-add pos vec)]
                (if (and (on-board? new-pos) (not (same-color? board pos new-pos)) (not (check? (move-piece board pos new-pos) new-pos))) (conj res new-pos) res)))
            #{}
            [[0 1] [0 -1] [1 0] [-1 0] [1 1] [1 -1] [-1 1] [-1 -1]]) #{}))
(defmethod semi-legal-moves :none [_ _] #{})

(defn legal? [state from to] (cond (keyword? from) (contains? (semi-legal-moves state (board/board-to-coord from)) (board/board-to-coord to)) (vector? from) (contains? (semi-legal-moves state from) to)))

(defn move-piece! "Mutate game-state to move a piece from 'from' to 'to'"
  [game-state from to]
  (let [piece (get-piece (:board @game-state) from)]
    (swap! game-state (fn [state pos piece] (assoc state :board (assoc-in (:board state) pos piece))) (cond (keyword? from) (board/board-to-coord from) (vector? from) from) 0)
    (swap! game-state (fn [state pos piece] (assoc state :board (assoc-in (:board state) pos piece))) (cond (keyword? to) (board/board-to-coord to) (vector? to) to) piece)))

(defn update-check-status! "Mutate game-state to reflect whether a check has been delivered"
  [game-state]
  (swap! game-state
         (fn [{:keys [board wk-pos bk-pos] :as state}]
           (assoc state :check
                  (if (check? board wk-pos) :w
                      (if (check? board bk-pos) :b nil))))))

(defn change-turn! "Update the turn of the game. Should be called after a turn is over"
  [game-state] (swap! game-state (fn [{turn :turn :as state}] (assoc state :turn (case turn :w :b :b :w)))))

(defn reset-game-state! "Takes an atom and resets it to a valid initial game state" [game-state]
  (reset! game-state {:board board/starting-position
                      :wqr-moved false
                      :wkr-moved false
                      :wk-moved false
                      :bqr-moved false
                      :bkr-moved false
                      :bk-moved false
                      :wk-pos [7 4]
                      :bk-pos [0 4]
                      :en-passantable nil
                      :turn :w
                      :check false
                      :abs-pins #{} ;;positions of absolutely pinned pieces
                      :rep-counter 0 ;;how many times a repetition has occured (draw if reaches 3)
                      :no-prog-counter 0 ;;how many turns without progress (draw if reaches 50)
                      :previous-boards nil ;;keep track of game history between progressions
                      }))
