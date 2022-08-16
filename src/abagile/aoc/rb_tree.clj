(ns abagile.aoc.rb-tree
  (:require
    [abagile.aoc.bst-tree :as bst]
    [abagile.aoc.util :as util]
    [clojure.core.match :refer [match]]
    [clojure.test :refer [deftest is]]))

#_:clj-kondo/ignore
(defn balance
  [tree]
  (match tree
    (:or
      {:rb :b :lt {:rb :r :lt {:rb :r :lt a :el x :rt b} :el y :rt c} :el z :rt d}
      {:rb :b :lt {:rb :r :lt a :el x :rt {:rb :r :lt b :el y :rt c}} :el z :rt d}
      {:rb :b :lt a :el x :rt {:rb :r :lt {:rb :r :lt b :el y :rt c} :el z :rt d}}
      {:rb :b :lt a :el x :rt {:rb :r :lt b :el y :rt {:rb :r :lt c :el z :rt d}}})
    {:rb :r :lt {:rb :b :lt a :el x :rt b} :el y :rt {:rb :b :lt c :el z :rt d}}
    :else tree))

(defn insert
  [tree x]
  (let [ins (fn ins [tree']
              (match tree'
                nil {:rb :r :el x :lt nil :rt nil}
                {:rb rb :el y :lt a :rt b} (cond
                                             (< x y) (balance {:rb rb :el y :lt (ins a) :rt b})
                                             (> x y) (balance {:rb rb :el y :lt a :rt (ins b)})
                                             :else   tree)))
        {:keys [el lt rt]} (ins tree)]
    {:rb :b :el el :lt lt :rt rt}))

(defn visualize
  ([tree] (visualize tree 0))
  ([{:keys [rb el lt rt] :as tree} depth]
   (if tree
     (str (visualize rt (inc depth)) (bst/tabs depth) el rb "\n" (visualize lt (inc depth)))
     (str (bst/tabs depth) "~\n"))))

(comment
  (-> (bst/->tree insert (util/range+ 1 15)) visualize print)
  (-> (bst/->tree insert [10 8 9 6 7 1 2 4 3 5]) visualize print)
  (-> (bst/->tree insert [5 3 4 1 2 6 7 8 9 10 11]) visualize print)
  (-> (bst/->tree insert (util/range+ 1 15)) bst/inorder-seq)
  (-> (bst/->tree insert (util/range+ 1 15)) bst/preorder-seq)
  (-> (bst/->tree insert (util/range+ 1 15)) bst/postorder-seq)
  (-> (bst/->tree insert (util/range+ 1 15)) bst/levelorder-seq))

(deftest tree-test)
  ;; (is (= -9 (-> (bst/->tree (util/range+ 1 10)) factor)))
  ;; (is (= 9  (-> (bst/->tree (util/range+ 10 1)) factor)))
  ;; (is (= -1 (-> (bst/->tree insert (util/range+ 1 10)) factor)))
  ;; (is (= 1  (-> (bst/->tree insert (util/range+ 10 1)) factor)))
  ;; (is (true?  (-> (bst/->tree [1 2 3]) rotate-left?)))
  ;; (is (false? (-> (bst/->tree [1 2 3]) rotate-right?)))
  ;; (is (false? (-> (bst/->tree [3 2 1]) rotate-left?)))
  ;; (is (true?  (-> (bst/->tree [3 2 1]) rotate-right?)))
  ;; (is (true?  (-> (bst/->tree [5 3 4]) rotate-right-left?)))
  ;; (is (false? (-> (bst/->tree [5 4 3]) rotate-right-left?)))
  ;; (is (true?  (-> (bst/->tree [3 5 4]) rotate-left-right?)))
  ;; (is (false? (-> (bst/->tree [3 4 5]) rotate-left-right?)))
  ;; (is (= (util/range+ 1 15)                    (-> (bst/->tree insert (util/range+ 1 15)) bst/inorder-seq)))
  ;; (is (= [8 4 2 1 3 6 5 7 12 10 9 11 14 13 15] (-> (bst/->tree insert (util/range+ 1 15)) bst/preorder-seq))))
