;;-------------------------------------------------------;;
;;-cljess.logic implements all the rules of a chess game-;;
;;-------------------------------------------------------;;
(ns cljess.logic
  (:require [cljess.piece :as piece]
            [cljess.board :as board]
            [reagent.core :as r]))

;;--------------------------;;
;;-General helper functions-;;
;;--------------------------;;

(defn get-piece "Get the piece (e.g. :wr) at pos on board"
  [board pos]
  (cond (vector? pos) (get-in board pos) (keyword? pos) (let [p (board/board-to-coord pos)] (get-in board p))))

(defn get-color "Get the color of the piece at pos on board"
  [board pos]
  (piece/color (get-piece board pos)))

(defn king-pos [{:keys [wk-pos bk-pos]} color] (case color :w wk-pos :b bk-pos))

(defn on-board? "Determine if a position is a valid board location"
  [[y x]]
  (and (>= x 0) (>= y 0) (< x 8) (< y 8)))

(defn same-color? "Return true if the pieces at pos1 and pos2 on board are the same color"
  [board pos1 pos2]
  (= (get-color board pos1) (get-color board pos2)))

(defn opposite-color? "Return true if the pieces at pos1 and pos2 on board are opposite colors"
  [board pos1 pos2]
  (let [other-color {:w :b :b :w}]
    (= (get-color board pos1) (other-color (get-color board pos2)))))

(defn vector-add "Perform elementwise addition of two length 2 vectors"
  [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(def dir-to-vec {:up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1]})

(def other-color {:w :b :b :w})

;;Single Move: [[from-y from-x] [to-y to-y]]
;;Complete Move: key: [from to]
;;               val: {:additional-action [:move single-move]-or-[:delete pos]
;;                     :effect-descriptors [effect keywords e.g. :progress, :king-move, etc.]}

;;------------------;;
;;-Movement helpers-;;
;;------------------;;

;;board modifiers
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
      (if-not (and (on-board? next-pos) (zero? (get-piece board next-pos)))
        moves
        (recur (assoc moves [pos next-pos] {}) (vector-add next-pos dir-vec))))))

(defn moves-in-directions [board pos dirs]
  (reduce merge (map #(moves-in-direction board pos %) dirs)))


;;-----------------;;
;;-Capture helpers-;;
;;-----------------;;

(defn capture-in-direction [board pos dir]
  (let [dir-vec (case dir :up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1])]
    (loop [next-pos (vector-add pos dir-vec)]
      (if (or (not (on-board? next-pos)) (same-color? board pos next-pos))
        {}
        (if (opposite-color? board pos next-pos)
          {[pos next-pos] {:effect-descriptors [:progress]}}
          (recur (vector-add next-pos dir-vec)))))))

(defn captures-in-directions [board pos dirs]
  (reduce merge (map #(capture-in-direction board pos %) dirs)))

;;--------------------------------------------;;
;;-Functions for finding and acting on checks-;;
;;--------------------------------------------;;

;;check finding helpers
(defn check-in-direction [board pos dir]
  (let [dir-vec (case dir :up [-1 0] :down [1 0] :left [0 -1] :right [0 1] :nw [-1 -1] :ne [-1 1] :sw [1 -1] :se [1 1])]
    (loop [capture #{} next-pos (vector-add pos dir-vec)]
      (if-not (on-board? next-pos)
        capture
        (if (zero? (get-piece board next-pos))
          (recur capture (vector-add next-pos dir-vec))
          (if (same-color? board pos next-pos)
            capture (conj capture (piece/typeof (get-piece board next-pos)))))))))

(defn checks-in-directions [board pos dirs]
  (reduce clojure.set/union (map #(check-in-direction board pos %) dirs)))

(defn knight-checks [board pos]
  (reduce (fn [res vec]
            (let [new-pos (vector-add pos vec)]
              (if (opposite-color? board pos new-pos)
                (conj res (piece/typeof (get-piece board new-pos)))
                res)))
          #{}
          [[1 2] [-1 2] [1 -2] [2 1] [-2 1] [2 -1] [-2 -1]]))

(defn pawn-checks [board pos]
  (let [moves (case (piece/color (get-piece board pos)) :w [[-1 1] [-1 -1]] :b [[1 1] [1 -1]])
        new-pos1 (vector-add pos (moves 0))
        new-pos2 (vector-add pos (moves 1))]
    (conj #{}
          (when (opposite-color? board pos new-pos1) (piece/typeof (get-piece board new-pos1)))
          (when (opposite-color? board pos new-pos2) (piece/typeof (get-piece board new-pos2))))))

(defn king-checks [board pos]
  (reduce (fn [res vec]
            (let [new-pos (vector-add pos vec)]
              (if (opposite-color? board pos new-pos)
                (conj res (piece/typeof (get-piece board new-pos)))
                res)))
          #{}
          [[0 1] [0 -1] [1 0] [-1 0] [1 1] [-1 1] [-1 -1] [1 -1]]))

;;check api functions
(defn check? "Determines whether the king at king-pos on board is in check"
  [board king-pos]
  (or (contains? (checks-in-directions board king-pos [:up :down :left :right]) :r)
      (contains? (checks-in-directions board king-pos [:nw :ne :sw :se]) :b)
      (contains? (checks-in-directions board king-pos [:up :down :left :right :nw :ne :sw :se]) :q)
      (contains? (knight-checks board king-pos) :n)
      (contains? (pawn-checks board king-pos) :p)
      (contains? (king-checks board king-pos) :k)))

(defn avoid-check "Prunes a set of moves to return only those that get the current side out of check"
  [board checked-king-pos moves]
  (reduce (fn [res move]
            (if-not (check? (move-piece board (key move)) checked-king-pos)
              (conj res move)
              res))
          {}
          moves))

(defn avoid-check-with-king "Prunes a set of king moves to return only those that get or remain out of check"
  [board moves]
  (reduce (fn [res move]
            (if-not (check? (move-piece board (key move)) (nth (key move) 1))
              (conj res move)
              res))
          {}
          moves))

(defn update-check "Returns a new game state to reflect whether a king is in check"
  [{:keys [board wk-pos bk-pos] :as game-state}]
  (assoc game-state :check (or (check? board wk-pos) (check? board bk-pos))))

;;-------------------------------------;;
;;-Functions for finding absolute pins-;;
;;-------------------------------------;;

(defn pin-in-direction [board kpos dir]
  (let [dir-vec (dir-to-vec dir)
        qb #{:q :b} qr #{:q :r}
        pinners (case dir :up qr :down qr :left qr :right qr :nw qb :ne qb :sw qb :se qb)]
    (loop [pinned-piece nil next-pos (vector-add kpos dir-vec)]
      (if (or (not (on-board? next-pos))
              (and (not (nil? pinned-piece)) (same-color? board kpos next-pos))
              (and (nil? pinned-piece) (opposite-color? board kpos next-pos))
              (and (not (nil? pinned-piece))
                   (opposite-color? board kpos next-pos)
                   (not (contains? pinners (piece/typeof (get-piece board next-pos))))))
        nil
        (if (and (contains? pinners (piece/typeof (get-piece board next-pos))) (opposite-color? board kpos next-pos))
          pinned-piece
          (if-not (zero? (get-piece board next-pos))
            (recur next-pos (vector-add next-pos dir-vec))
            (recur pinned-piece (vector-add next-pos dir-vec))))))))

(defn update-abs-pins "Returns a new game state to reflect all absolute pins on the board"
  [{:keys [board wk-pos bk-pos] :as game-state}]
  (let [dirs [:up :down :left :right :nw :ne :sw :se]]
    (assoc game-state :abs-pins
           (disj (clojure.set/union
                  (reduce (fn [res dir] (conj res (pin-in-direction board wk-pos dir))) #{} dirs)
                  (reduce (fn [res dir] (conj res (pin-in-direction board bk-pos dir))) #{} dirs)) nil))))

;;-----------------------;;
;;-Legal move generation-;;
;;-----------------------;;

;;overly verbose helper for castling
(defn castle? "Returns true if the designated castle is possible"
  [{:keys [check wkr-moved wqr-moved wk-moved bkr-moved bqr-moved bk-moved board]} color side]
  (if check
    false
    (case color
      :w (if wk-moved
           false
           (case side
             :king-side (if wkr-moved
                          false
                          (and
                           (not (check? (move-piece board [7 4] [7 5]) [7 5]))
                           (not (check? (move-piece board [7 4] [7 6]) [7 6]))
                           (zero? (get-piece board [7 5]))
                           (zero? (get-piece board [7 6]))
                           (= :wr (get-piece board [7 7]))))
             :queen-side (if wqr-moved
                           false
                           (and
                            (not (check? (move-piece board [7 4] [7 3]) [7 3]))
                            (not (check? (move-piece board [7 4] [7 2]) [7 2]))
                            (zero? (get-piece board [7 3]))
                            (zero? (get-piece board [7 2]))
                            (zero? (get-piece board [7 1]))
                            (= :wr (get-piece board [7 0]))))))
      :b (if bk-moved
           false
           (case side
             :king-side (if bkr-moved
                          false
                          (and
                           (not (check? (move-piece board [0 4] [0 5]) [0 5]))
                           (not (check? (move-piece board [0 4] [0 6]) [0 6]))
                           (zero? (get-piece board [0 5]))
                           (zero? (get-piece board [0 6]))
                           (= :br (get-piece board [0 7]))))
             :queen-side (if bqr-moved
                           false
                           (and
                            (not (check? (move-piece board [0 4] [0 3]) [0 3]))
                            (not (check? (move-piece board [0 4] [0 2]) [0 2]))
                            (zero? (get-piece board [0 3]))
                            (zero? (get-piece board [0 2]))
                            (zero? (get-piece board [0 1]))
                            (= :br (get-piece board [0 0])))))))))

(defmulti legal-moves (fn [{board :board} pos] (piece/typeof (get-piece board pos))))

(defmethod legal-moves :b ;;bishop moves
  [{:keys [board turn check abs-pins] :as state} pos]
  (if (not= turn (get-color board pos))
    {}
    (let [bishop-dirs [:nw :ne :sw :se]
          moves (merge (moves-in-directions board pos bishop-dirs) (captures-in-directions board pos bishop-dirs))]
      (if (or check (contains? abs-pins pos))
        (avoid-check board (king-pos state turn) moves)
        moves))))

(defmethod legal-moves :q ;;queen moves
  [{:keys [board turn check abs-pins] :as state} pos]
  (if (not= turn (get-color board pos))
    {}
    (let [queen-dirs [:left :right :up :down :nw :ne :sw :se]
          moves (merge (moves-in-directions board pos queen-dirs) (captures-in-directions board pos queen-dirs))]
      (if (or check (contains? abs-pins pos))
        (avoid-check board (king-pos state turn) moves)
        moves))))

(defmethod legal-moves :n ;;knight moves
  [{:keys [board turn check abs-pins] :as state} pos]
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

(defmethod legal-moves :r ;;rook moves
  [{:keys [board turn check abs-pins] :as state} pos]
  (if (not= turn (get-color board pos))
    {}
    (let [rook-dirs [:up :down :left :right]
          moves (merge (moves-in-directions board pos rook-dirs) (captures-in-directions board pos rook-dirs))
          lmoves (if (or check (contains? abs-pins pos)) (avoid-check board (king-pos state turn) moves) moves)]
      (case turn
        :w (if (= pos [7 7])
             (into {} (map (fn [move] (update-in move [1 :effect-descriptors] #(into [] (conj % :wkr-moved)))) lmoves))
             (if (= pos [7 0])
               (into {}(map (fn [move] (update-in move [1 :effect-descriptors] #(into [] (conj % :wqr-moved)))) lmoves))
               lmoves))
        :b (if (= pos [0 7])
             (into {} (map (fn [move] (update-in move [1 :effect-descriptors] #(into [] (conj % :bkr-moved)))) lmoves))
             (if (= pos [0 0])
               (into {} (map (fn [move] (update-in move [1 :effect-descriptors] #(into [] (conj % :bqr-moved)))) lmoves))
               lmoves))))))

(defmethod legal-moves :k ;;king moves
  [{board :board turn :turn :as state} pos]
  (if (not= turn (get-color board pos))
    {}
    (merge (avoid-check-with-king
            board
            (reduce (fn [res vec]
                      (let [new-pos (vector-add pos vec)]
                        (if (zero? (get-piece board new-pos))
                          (assoc res
                                 [pos new-pos]
                                 {:effect-descriptors [(case turn :w :wk-moved :b :bk-moved)]})
                          (if (opposite-color? board pos new-pos)
                            (assoc res
                                   [pos new-pos]
                                   {:effect-descriptors [:progress (case turn :w :wk-moved :b :bk-moved)]})
                            res))))
                   {}
                   [[0 1] [0 -1] [1 0] [-1 0] [1 1] [1 -1] [-1 1] [-1 -1]]))
           (case turn
             :w (merge
                 (if (castle? state :w :king-side)
                   {[[7 4] [7 6]] {:additional-action [:move [[7 7] [7 5]]] :effect-descriptors [:wk-moved]}}
                   {})
                 (if (castle? state :w :queen-side)
                   {[[7 4] [7 2]] {:additional-action [:move [[7 0] [7 3]]] :effect-descriptors [:wk-moved]}}
                   {}))
             :b (merge
                 (if (castle? state :b :king-side)
                   {[[0 4] [0 6]] {:additional-action [:move [[0 7] [0 5]]] :effect-descriptors [:bk-moved]}}
                   {})
                 (if (castle? state :b :queen-side)
                   {[[0 4] [0 2]] {:additional-action [:move [[0 0] [0 3]]] :effect-descriptors [:bk-moved]}}
                   {}))))))

(defmethod legal-moves :p ;;pawn moves
  [{:keys [board en-passantable turn check abs-pins] :as state} [y x :as pos]]
  (let [color (get-color board pos)]
    (if (not= turn color)
      {}
      (let [progress-y (case color :w #(- %1 %2) :b #(+ %1 %2))
            starting-y (case color :w 6 :b 1)
            queening-y (case color :w 0 :b 7)
            moves (merge
                   (let [[new-y _ :as new-pos] [(progress-y y 1) x]] ;;pawn push 1 forward
                     (if (zero? (get-piece board new-pos))
                       {[pos new-pos] {:effect-descriptors (conj (if (= new-y queening-y) [:queen] []) :progress)}}
                       {}))
                   (if (and (= y starting-y)
                            (zero? (get-piece board [(progress-y y 1) x]))
                            (zero? (get-piece board [(progress-y y 2) x]))) ;;initial pawn push 2 forward
                     {[pos [(progress-y y 2) x]] {:effect-descriptors [:progress :en-passantable]}}
                     {})
                   (let [[new-y _ :as new-pos] [(progress-y y 1) (inc x)]] ;;en-passant capture right
                     (if (= new-pos en-passantable)
                       {[pos new-pos] {:additional-action [:delete [y (inc x)]] :effect-descriptors [:progress]}}
                       {}))
                   (let [[new-y _ :as new-pos] [(progress-y y 1) (dec x)]] ;;en-passant capture left
                     (if (= new-pos en-passantable)
                       {[pos new-pos] {:additional-action [:delete [y (dec x)]] :effect-descriptors [:progress]}}
                       {}))
                   (let [[new-y _ :as new-pos] [(progress-y y 1) (inc x)]] ;;normal capture right
                     (if (opposite-color? board pos new-pos)
                       {[pos new-pos] {:effect-descriptors (conj (if (= new-y queening-y) [:queen] []) :progress)}}
                       {}))
                   (let [[new-y _ :as new-pos] [(progress-y y 1) (dec x)]] ;;normal capture left
                     (if (opposite-color? board pos new-pos)
                       {[pos new-pos] {:effect-descriptors (conj (if (= new-y queening-y) [:queen] []) :progress)}}
                       {})))]
        (if (or check (contains? abs-pins pos))
          (avoid-check board (king-pos state turn) moves)
          moves)))))

(defmethod legal-moves :none [_ _] {})

(defn all-legal-moves "Generate all legal moves for a game state"
  [state]
  (loop [y 0 res {}]
    (if (<= y 7)
      (recur (inc y) (merge res (loop [x 0 moves {}]
                                  (if (<= x 7)
                                    (recur (inc x) (merge moves (legal-moves state [y x])))
                                    moves))))
      res)))

;;----------------------;;
;;-Legal move execution-;;
;;----------------------;;

(defn legal? [state from to] (contains? (legal-moves state from) [from to]))

(defn do-action [{board :board :as state} [action arg :as a]]
  (if (nil? a)
    state
    (case action
      :move (assoc state :board (move-piece board arg))
      :delete (assoc state :board (delete-piece board arg)))))

(defmulti do-effect (fn [state move effect] effect))
(defmethod do-effect :wk-moved [state [_ to] _] (assoc state :wk-moved true :wk-pos to))
(defmethod do-effect :bk-moved [state [_ to] _] (assoc state :bk-moved true :bk-pos to))
(defmethod do-effect :wkr-moved [state _ _] (assoc state :wkr-moved true))
(defmethod do-effect :wqr-moved [state _ _] (assoc state :wqr-moved true))
(defmethod do-effect :bkr-moved [state _ _] (assoc state :bkr-moved true))
(defmethod do-effect :bqr-moved [state _ _] (assoc state :bqr-moved true))
(defmethod do-effect :progress [state _ _] (assoc state :no-prog-counter 0 :previous-boards {}))
(defmethod do-effect :en-passantable [state [[y x] _] _] (assoc state :en-passantable [(if (= y 1) 2 5) x]))
(defmethod do-effect :queen [state [_ [y x :as to]] _] (assoc-in state [:board y x] (if (= y 0) :wq :bq)))

(defn update-effects [state move effect-descriptors]
  (if (nil? effect-descriptors)
    state
    (reduce (fn [res effect] (do-effect res move effect)) state effect-descriptors)))

(defn record-board [{:keys [board previous-boards] :as state}]
  (if (contains? previous-boards board)
    (update-in state [:previous-boards board] #(inc %))
    (update-in state [:previous-boards] #(assoc % board 1))))

(defn update-result [{:keys [check board turn previous-boards no-prog-counter] :as state}]
  (let [moves (all-legal-moves state)]
    (if (or (>= no-prog-counter 100) (= (previous-boards board) 3) (and (not check) (= moves {})))
      (assoc state :result :draw)
      (if (and check (= moves {}))
        (assoc state :result (other-color turn))
        state))))

(defn make-move
  ([{:keys [board turn no-prog-counter] :as state} from to]
   (let [move [from to]
         move-info ((legal-moves state from) move)
         additional-action (:additional-action move-info)
         effect-descriptors (:effect-descriptors move-info)]
     (update-result
      (record-board
       (update-abs-pins
        (update-check
         (update-effects
          (do-action
           (assoc state
                  :board (move-piece board move)
                  :turn (other-color turn)
                  :en-passantable nil
                  :no-prog-counter (inc no-prog-counter))
           additional-action)
          move effect-descriptors)))))))
  ([{:keys [board turn no-prog-counter] :as state} move-description]
   (let [move (key move-description)
         move-info (val move-description)
         additional-action (:additional-action move-info)
         effect-descriptors (:effect-descriptors move-info)]
     (update-result
      (record-board
       (update-abs-pins
        (update-check
         (update-effects
          (do-action
           (assoc state
                  :board (move-piece board move)
                  :turn (other-color turn)
                  :en-passantable nil
                  :no-prog-counter (inc no-prog-counter))
           additional-action)
          move effect-descriptors))))))))

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
                      :no-prog-counter 0 ;;how many turns without progress (draw if reaches 50)
                      :previous-boards {} ;;keep track of game history between progressions
                      :result nil}))
