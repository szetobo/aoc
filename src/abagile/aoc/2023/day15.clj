(ns abagile.aoc.2023.day15
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]
   [clojure.test :refer [deftest is]]))

(def sample (-> "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7" (cs/split #",")))
(def input (-> (util/read-input "2023/day15.txt") cs/trim (cs/split #",")))

(defn HASH
  ([steps] (HASH steps 0))
  ([steps v]
   (reduce #(-> %1 (+ (int %2)) (* 17) (mod 256)) v (seq steps))))

(deftest example1
  (is (= (HASH "HASH") 52))
  (is (= (HASH "rn=1") 30))
  (is (= (HASH "cm-")  253))
  (is (= (HASH "qp=3") 97))
  (is (= (HASH "cm=2") 47)))

(defn part1
  []
  (time (->> input (map HASH) (reduce +))))


(defn parse
  [step]
  (let [[label op len] (nfirst (re-seq #"(\w+)([=-])(\d+)?" step))]
    [(keyword label) (HASH label) (keyword op) (when len (read-string len))]))

(comment
  {:hashes {:rn 0, :cm 0, :ot 3, :ab 3, :pc 3},
   :focal-lens {:rn 1, :qp 3, :cm 2, :pc 6, :ot 7, :ab 5},
   :boxes
   [[:rn :cm] [] [] [:ot :ab :pc] ,,,]})

(defn HASHMAP
  [state [label hash-num op len]]
  (case op
    := (cond-> (assoc-in state [:focal-lens label] len)
        (nil? (get-in state [:hashes label]))
        (-> (assoc-in [:hashes label] hash-num)
            (update-in [:boxes hash-num] conj label)))
    :- (cond-> state
         (get-in state [:hashes label])
         (-> (update-in [:hashes] dissoc label)
             (update-in [:boxes hash-num] #(vec (remove #{label} %)))))))

(defn focusing-power
  [{:keys [hashes focal-lens boxes]}]
  (let [slots (->> (for [box (remove empty? boxes)]
                     (map-indexed #(vector %2 (inc %1)) box))
                   (apply concat)
                   (into {}))]
    (reduce-kv #(+ %1 (* (inc %3) (focal-lens %2) (slots %2))) 0 hashes)))

(deftest example2
  (is (= (HASH "rn") 0))
  (is (= (HASH "qp") 1))
  (is (= (HASH "pc") 3))
  (is (= (HASH "ot") 3))
  (is (= (parse "rn=1") [:rn 0 := 1]))
  (is (= (parse "cm-") [:cm 0 :- nil])))

(defn part2
  []
  (time (->> input
             (map parse)
             (reduce HASHMAP {:hashes {} :focal-lens {} :boxes (vec (repeat 256 []))})
             focusing-power)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
