(ns abagile.aoc.bst-tree
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]
    [clojure.test :refer [deftest is]])
  (:refer-clojure :exclude [contains? min max count]))

(defn insert
  [{:keys [el lt rt] :as tree} x]
  (cond
    (nil? tree) {:el x :lt nil :rt nil}
    (< x el)    {:el el :lt (insert lt x) :rt rt}
    (> x el)    {:el el :lt lt :rt (insert rt x)}
    :else       tree))

(defn contains?
  [{:keys [el lt rt] :as tree} x]
  (cond
    (nil? tree) false
    (< x el)    (recur lt x)
    (> x el)    (recur rt x)
    :else       true))

(defn min
  [{:keys [el lt]}]
  (if lt (recur lt) el))

(defn max
  [{:keys [el rt]}]
  (if rt (recur rt) el))

(defn count
  [{:keys [lt rt] :as tree}]
  (if tree
    (+ 1 (count lt) (count rt))
    0))

(defn height
  [{:keys [lt rt] :as tree}]
  (if tree
    (inc (clojure.core/max (height lt) (height rt)))
    0))

(defn ->tree
  ([coll]   (->tree insert coll))
  ([f coll] (reduce f nil coll)))

(def tabs #(cs/join (->> (apply str (repeat 8 " ")) (repeat %))))

(defn visualize
  ([tree] (visualize tree 0))
  ([{:keys [el lt rt] :as tree} depth]
   (if tree
     (str (visualize rt (inc depth)) (tabs depth) el "\n" (visualize lt (inc depth)))
     (str (tabs depth) "~\n"))))

(defn preorder-seq
  [{:keys [el lt rt] :as tree}]
  (when tree
    (concat [el] (preorder-seq lt) (preorder-seq rt))))

(defn inorder-seq
  [{:keys [el lt rt] :as tree}]
  (when tree
    (concat (inorder-seq lt) [el] (inorder-seq rt))))

(defn postorder-seq
  [{:keys [el lt rt] :as tree}]
  (when tree
    (concat (postorder-seq lt) (postorder-seq rt) [el])))

(defn queue
  ([] clojure.lang.PersistentQueue/EMPTY)
  ([coll] (reduce conj (queue) coll))
  ([que coll] (reduce conj que coll)))

(defn levelorder-seq
  [{:keys [el lt rt] :as tree}]
  (when tree
    (loop [que (queue (remove nil? [lt rt])) res (queue [el])]
      (if (empty? que)
        (seq res)
        (let [{:keys [el lt rt]} (peek que)]
          (recur (queue (pop que) (remove nil? [lt rt])) (conj res el)))))))

(comment
  (-> (->tree (util/range+ 1 10)) visualize print)
  (-> (->tree (shuffle (util/range+ 1 10))) visualize print)
  (-> (->tree [10 8 9 6 7 1 2 4 3 5]) visualize print)
  (-> (->tree [5 3 4 1 2 6 7 8 9 10 11]) visualize print)
  (-> (->tree (util/range+ 1 15)) visualize print)
  (-> (->tree (util/range+ 1 15)) inorder-seq)
  (-> (->tree (util/range+ 1 15)) preorder-seq)
  (-> (->tree (util/range+ 1 15)) levelorder-seq))

(deftest tree-test
  (is (= 10 (-> (->tree (util/range+ 1 10)) height)))
  (is (= 10 (-> (->tree (util/range+ 1 10)) count)))
  (is (= 1  (-> (->tree (util/range+ 1 10)) min)))
  (is (= 10 (-> (->tree (util/range+ 1 10)) max)))
  (let [tree (->tree (util/range+ 1 10))]
    (doseq [n (util/range+ 1 10)]
      (is (true? (contains? tree n))) (str n " not in tree")))
  (is (= [1 2 3 4 5 6 7 8 9 10] (-> (->tree [10 8 9 6 7 1 2 4 3 5]) inorder-seq)))
  (is (= [10 8 6 1 2 4 3 5 7 9] (-> (->tree [10 8 9 6 7 1 2 4 3 5]) preorder-seq)))
  (is (= [3 5 4 2 1 7 6 9 8 10] (-> (->tree [10 8 9 6 7 1 2 4 3 5]) postorder-seq)))
  (is (= [10 8 6 9 1 7 2 4 3 5] (-> (->tree [10 8 9 6 7 1 2 4 3 5]) levelorder-seq))))
