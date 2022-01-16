(ns abagile.aoc.2015.day17
  (:gen-class)
  (:require
    [abagile.aoc.algo :as algo]
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input "2015/day17.sample.txt"))
(def input  (util/read-input "2015/day17.txt"))

(defn parse
  [data]
  (->> (re-seq #"(\d+)" data) (map #(read-string (second %)))))

(defn part1 []
  (time
    (->> (parse input) (algo/subset-sum-01 150) count)))

(defn part2 []
  (time
    (->> (parse input) (algo/subset-sum-01 150)
         (#(let [counts  (group-by count %)
                 min-cnt (apply min (keys counts))]
             (count (counts min-cnt)))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest test-sample
  (is (= 4 (->> (parse sample) (algo/subset-sum-01 25) count)))
  (is (= 3 (->> (parse sample) (algo/subset-sum-01 25)
                (#(let [counts  (group-by count %)
                        min-cnt (apply min (keys counts))]
                    (count (counts min-cnt))))))))
