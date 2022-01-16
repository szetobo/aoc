(ns abagile.aoc.2015.day16
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.set :as set]
    [clojure.test :refer [deftest is]]))

(def input (util/read-input-split "2015/day16.txt" #"\n"))

(def target {:children 3 :cats     7 :samoyeds 2 :pomeranians 3 :akitas   0
             :vizslas  0 :goldfish 5 :trees    3 :cars        2 :perfumes 1})

(defn parse
  [data]
  (into {} (map vector
             (map #(->> (re-find #"Sue (\d+):" %) second read-string) data)
             (map #(->> (re-seq #"(\w+): (\d+)" %)
                        (map (fn [[_ k v]] [(keyword k) (read-string v)]))
                        (into {}))
               data))))

(defn part1 []
  (time
    (let [ts (set target)]
     (->> (parse input)
          (filter (fn [[_ v]] (set/subset? (set v) ts)))
          first))))

(defn part2 []
  (time
    (->> (parse input)
         (filter (fn [[_ m]]
                   (every? (fn [[k v]]
                             (let [tv (target k)]
                               (case k
                                 :cats :trees           (> v tv)
                                 :pomeranians :goldfish (< v tv)
                                 (= v tv))))
                     m)))
         first)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 0 0)))
