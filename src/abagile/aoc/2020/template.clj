(ns abagile.aoc.2020.template
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest run-tests is]]
    [instaparse.core :as insta]))

; (def input (->> (util/read-input-split-lines "2020/day.txt")))

(def sample [])

(def rule-parser (insta/parser "S = 'testing'"))

(def transform-opts {:I identity})

(defn parse [data]
  (->> (map rule-parser data)
       (map (partial insta/transform transform-opts))))

(comment)

(defn part1 [])

(defn part2 [])

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (is (= 1 1)))

(deftest test-input
  (is (= 1 1)))

(run-tests)
