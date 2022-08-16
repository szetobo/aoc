(ns abagile.aoc.avl-tree
  (:require
    [abagile.aoc.bst-tree :as bst]
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(defrecord Node [elm left right])

(defn factor
  [{:keys [left right]}]
  (- (bst/height left) (bst/height right)))

(defn rotate-left
  [{:keys [elm left right] :as tree}]
  (if right
    (->Node (:elm right) (->Node elm left (:left right)) (:right right))
    tree))

(defn rotate-right
  [{:keys [elm left right] :as tree}]
  (if left
    (->Node (:elm left) (:left left) (->Node elm (:right left) right))
    tree))

(def rotate-left?       #(< (factor %) -1))
(def rotate-left-right? #(and (rotate-left? %) (-> % :right factor (> 0))))

(def rotate-right?      #(> (factor %) 1))
(def rotate-right-left? #(and (rotate-right? %) (-> % :left factor (< 0))))

(defn balance
  [{:keys [elm left right] :as tree}]
  (cond
    (rotate-right-left? tree) (rotate-right (->Node elm (rotate-left left) right))
    (rotate-left-right? tree) (rotate-left (->Node elm left (rotate-right right)))
    (rotate-left? tree)       (rotate-left tree)
    (rotate-right? tree)      (rotate-right tree)
    :else                     tree))

(defn insert
  [{:keys [elm left right] :as tree} v]
  (cond
    (nil? tree) (->Node v nil nil)
    (< v elm)   (balance (->Node elm (insert left v) right))
    (> v elm)   (balance (->Node elm left (insert right v)))
    :else       tree))

(comment
  (-> (bst/->tree (util/range+ 1 10)) bst/visualize print)
  (-> (bst/->tree (util/range+ 10 1)) bst/visualize print)
  (-> (bst/->tree insert (util/range+ 1 10)) bst/visualize print)
  (-> (bst/->tree insert (util/range+ 10 1)) bst/visualize print)
  (-> (bst/->tree insert (shuffle (util/range+ 1 15))) bst/visualize print)
  (-> (reductions insert nil (util/range+ 1 10)))
  (-> (bst/->tree insert [10 8 9 6 7 1 2 4 3 5]) bst/visualize print)
  (-> (bst/->tree insert [5 3 4 1 2 6 7 8 9 10 11]) bst/visualize print)
  (-> (bst/->tree insert (util/range+ 1 15)) bst/visualize print)
  (-> (bst/->tree insert (util/range+ 1 15)) bst/inorder-seq)
  (-> (bst/->tree insert (util/range+ 1 15)) bst/preorder-seq))

(deftest tree-test
  (is (= -9 (-> (bst/->tree (util/range+ 1 10)) factor)))
  (is (= 9  (-> (bst/->tree (util/range+ 10 1)) factor)))
  (is (= -1 (-> (bst/->tree insert (util/range+ 1 10)) factor)))
  (is (= 1  (-> (bst/->tree insert (util/range+ 10 1)) factor)))
  (is (true?  (-> (bst/->tree [1 2 3]) rotate-left?)))
  (is (false? (-> (bst/->tree [1 2 3]) rotate-right?)))
  (is (false? (-> (bst/->tree [3 2 1]) rotate-left?)))
  (is (true?  (-> (bst/->tree [3 2 1]) rotate-right?)))
  (is (true?  (-> (bst/->tree [5 3 4]) rotate-right-left?)))
  (is (false? (-> (bst/->tree [5 4 3]) rotate-right-left?)))
  (is (true?  (-> (bst/->tree [3 5 4]) rotate-left-right?)))
  (is (false? (-> (bst/->tree [3 4 5]) rotate-left-right?)))
  (is (= (util/range+ 1 15)                    (-> (bst/->tree insert (util/range+ 1 15)) bst/inorder-seq)))
  (is (= [8 4 2 1 3 6 5 7 12 10 9 11 14 13 15] (-> (bst/->tree insert (util/range+ 1 15)) bst/preorder-seq))))
