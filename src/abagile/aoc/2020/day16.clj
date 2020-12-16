(ns abagile.aoc.2020.day16
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.set :as s]
    [clojure.string :as cs]))

(def sample1 [["class: 1-3 or 5-7"
               "row: 6-11 or 33-44"
               "seat: 13-40 or 45-50"]
              ["your ticket:"
               "7,1,14"
               "nearby tickets:"
               "7,3,47"
               "40,4,50"
               "55,2,20"
               "38,6,12"]])

(def sample2 [["class: 0-1 or 4-19"
               "row: 0-5 or 8-19"
               "seat: 0-13 or 16-19"]
              ["your ticket:"
               "11,12,13"
               "nearby tickets:"
               "3,9,18"
               "15,1,5"
               "5,14,9"]])

(def input (->> (cs/split (util/read-input "2020/day16.txt") #"your ticket:")
                (map cs/split-lines)))

(defn parse1 [s] (->> (re-seq #"(\d+)-(\d+)" s)
                      (mapcat rest)
                      (map util/parse-int)))

(defn parse2 [s] (->> (re-seq #"(\d+)" s)
                      (mapcat rest)
                      (map util/parse-int)))

(defn parse3 [s] (->> (re-find #"(.+): (\d+)-(\d+) or (\d+)-(\d+)" s)
                      rest))

(defn invalid-vals [data]
  (let [s1  (->> (map parse1 (first data))
                 (mapcat #(partition 2 %))
                 (reduce (fn [res [x z]] (into res (range x (inc z)))) #{}))
        lst (->> (map parse2 (second data))
                 (filter seq))]
    (->> (mapcat #(map (fn [n] (if (s1 n) nil n)) %) lst)
         (filter some?))))

(defn part1 []
  (reduce + (invalid-vals input)))


(defn invalid-set [data]
  (into #{} (invalid-vals data)))

(comment
  (invalid-set input))

(defn part2 []
  (let [data input
        s1  (->> (map parse3 (first data))
                 (reduce (fn [res [f n1 n2 n3 n4]]
                           (let [n1 (util/parse-int n1)
                                 n2 (util/parse-int n2)
                                 n3 (util/parse-int n3)
                                 n4 (util/parse-int n4)]
                             (assoc res f [n1 n2 n3 n4])))
                         {}))
        [yl & nl] (->> (map parse2 (second data))
                       (filter seq))
        yl (vec yl)
        nl (->> nl
                (filter #(every? (comp nil? (invalid-set data)) %))
                (reduce (fn [res nos]
                          (if (coll? (first res))
                            (mapv conj res nos)
                            (concat (partition 2 2 (interleave nos res)))))))
        cm (for [[i n] (map-indexed vector nl) [k v] s1
                 :let [[n1 n2 n3 n4] v]
                 :when (every? #(or (<= n1 % n2) (<= n3 % n4)) n)]
             [i k])]
    (->> (loop [fm cm
                res {}]
           (if-not (seq fm)
             res
             (let [cl (map first (filter #(= (count %) 1) (partition-by first fm)))
                   res' (reduce (fn [res [idx field]] (assoc res idx field)) res cl)
                   fm' (filter (fn [[_ field]] (every? #(not= field %) (map second cl))) fm)]
               (recur fm' res'))))
         (filter (fn [[_ v]] (re-find #"departure" v)))
         keys
         (map #(nth yl %))
         (reduce *))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
