(ns cljess.piece)

(def image
  {:wp "https://upload.wikimedia.org/wikipedia/commons/4/45/Chess_plt45.svg"
   :bp "https://upload.wikimedia.org/wikipedia/commons/c/c7/Chess_pdt45.svg"
   :wn "https://upload.wikimedia.org/wikipedia/commons/7/70/Chess_nlt45.svg"
   :bn "https://upload.wikimedia.org/wikipedia/commons/e/ef/Chess_ndt45.svg"
   :wb "https://upload.wikimedia.org/wikipedia/commons/b/b1/Chess_blt45.svg"
   :bb "https://upload.wikimedia.org/wikipedia/commons/9/98/Chess_bdt45.svg"
   :wr "https://upload.wikimedia.org/wikipedia/commons/7/72/Chess_rlt45.svg"
   :br "https://upload.wikimedia.org/wikipedia/commons/f/ff/Chess_rdt45.svg"
   :wq "https://upload.wikimedia.org/wikipedia/commons/1/15/Chess_qlt45.svg"
   :bq "https://upload.wikimedia.org/wikipedia/commons/4/47/Chess_qdt45.svg"
   :wk "https://upload.wikimedia.org/wikipedia/commons/4/42/Chess_klt45.svg"
   :bk "https://upload.wikimedia.org/wikipedia/commons/f/f0/Chess_kdt45.svg"})

(def color
  {:wp :w :bp :b
   :wn :w :bn :b
   :wb :w :bb :b
   :wr :w :br :b
   :wq :w :bq :b
   :wk :w :bk :b})

(defn typeof "Get the type of a piece (e.g. :wr -> :r)"
  [piece] (if (keyword? piece) (keyword (subs (name piece) 1 2)) :none))
