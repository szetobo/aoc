(ns abagile.aoc.2015.template
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]
    [instaparse.core :as insta]))

; (def input (->> (util/read-input-split-lines "2015/day.txt")))

(def sample [])

(def rule-parser (insta/parser "S = 'testing'"))

(def transform-opts {:I identity})

(defn parse [data]
  (->> (map rule-parser data)
       (map (partial insta/transform transform-opts))))

(defn part1 [])

(defn part2 [])

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (is (= 1 1)))

(deftest test-input
  (is (= 1 1)))
