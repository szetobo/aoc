(ns abagile.aoc.2015.day11
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample1 "abcdefgh")
(def sample2 "ghijklmn")
(def input   "hxbxwxba")

(def succ-digits
  (assoc
    (->> (util/range+ (int \a) (int \z)) (map char) (partition 2 1) (map vec) (into {}))
    \h \j \k \m \n \p \z \a))

(defn succ
  [s]
  (loop [s (vec s) res ""]
    (if (empty? s)
      (str \a res)
      (let [last-digit (-> s peek succ-digits) s (pop s)]
        (if (= last-digit \a)
          (recur s (str \a res))
          (str (apply str s) last-digit res))))))

(defn valid? [s]
  (boolean
    (and
      (not (re-seq #"[iol]" s))
      (some (fn [[x y z]] (and (= (-> x int inc) (int y)) (= (-> y int inc) (int z)))) (partition 3 1 s))
      (let [pairs (->> (re-seq #"(\w)(?=\1)" s) (map first) (partition 2 1))]
        (and (seq pairs) (every? #(apply not= %) pairs))))))

(def iterate-pwd #(->> (iterate succ %) (filter valid?)))

(defn part1
  []
  (time
    (first (iterate-pwd input))))

(defn part2
  []
  (time
    (first (drop 1 (iterate-pwd input)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= false (valid? "hijklmmn")))
  (is (= false (valid? "abbceffg")))
  (is (= false (valid? "abbcegjk")))
  (is (= "abcdffaa" (first (iterate-pwd sample1))))
  (is (= "ghjaabcc" (first (iterate-pwd sample2)))))
