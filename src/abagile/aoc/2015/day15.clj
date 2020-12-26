(ns abagile.aoc.2015.day15
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]
    [instaparse.core :as insta]))

(def sample ["Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8"
             "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"])

(def input ["Sprinkles: capacity 2, durability 0, flavor -2, texture 0, calories 3"
            "Butterscotch: capacity 0, durability 5, flavor -3, texture 0, calories 3"
            "Chocolate: capacity 0, durability 0, flavor 5, texture -1, calories 8"
            "Candy: capacity 0, durability -1, flavor 0, texture 5, calories 8"])

(def rule-parser (insta/parser "S = IN <':'> PROPS
                                IN = #'\\w+'
                                PROPS = PROP | PROP (<','> PROP)+
                                <space> = <#'\\s*'>
                                PROP = PT QTY
                                PT = space #'\\w+' space
                                QTY = #'[-0-9]+'"))

(def transform-opts {:S hash-map
                     :IN keyword
                     :PROPS merge
                     :PROP hash-map
                     :PT keyword
                     :QTY util/parse-int})

(defn parse [data]
  (->> (map rule-parser data)
       (map (partial insta/transform transform-opts))
       (reduce merge)))

(defn pick-scores [data ks]
  (->> (reduce-kv (fn [r k v] (assoc r k (map v ks))) {} data)
       vals))

(defn part1 []
  (let [data (parse input)
        scores (pick-scores data [:flavor :capacity :durability :texture])]
    (->> (for [a (range 101)
               b (range 101)
               c (range 101)
               d (range 101)
               :when (= (+ a b c d) 100)]
          [(->> (apply map #(max 0 (+ (* a %1) (* b %2) (* c %3) (* d %4))) scores)
                (reduce *))
           a b c d])
         (sort-by first >)
         first)))

(defn part2 []
  (let [data (parse input)
        scores (pick-scores data [:flavor :capacity :durability :texture])
        calories (flatten (pick-scores data [:calories]))]
    (->> (for [a (range 101)
               b (range 101)
               c (range 101)
               d (range 101)
               :when (= (+ a b c d) 100)
               :when (= 500 (reduce + (map #(* %1 %2) [a b c d] calories)))]
          [(->> (apply map #(max 0 (+ (* a %1) (* b %2) (* c %3) (* d %4))) scores)
                (reduce *))
           a b c d])
         (sort-by first >)
         first)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (is (= (parse sample) {:Butterscotch
                         {:calories 8, :flavor 6, :capacity -1, :durability -2, :texture 3},
                         :Cinnamon
                         {:calories 3, :flavor -2, :capacity 2, :durability 3, :texture -1}}))
  (is (= (pick-scores (parse sample) [:flavor :capacity :durability :texture])
         [[6 -1 -2 3] [-2 2 3 -1]]))
  (is (= (flatten (pick-scores (parse sample) [:calories])) [8 3])))

(deftest test-input
  (is (= (parse input) {:Butterscotch
                        {:calories 3, :flavor -3, :capacity 0, :durability 5, :texture 0},
                        :Candy
                        {:calories 8, :flavor 0, :capacity 0, :durability -1, :texture 5},
                        :Sprinkles
                        {:calories 3, :flavor -2, :capacity 2, :durability 0, :texture 0},
                        :Chocolate
                        {:calories 8, :flavor 5, :capacity 0, :durability 0, :texture -1}})))
