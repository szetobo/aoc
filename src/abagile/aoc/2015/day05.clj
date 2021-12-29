(ns abagile.aoc.2015.day05
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def input (->> (util/read-input-split "2015/day05.txt" #"\n")))

(defn nice?
  [s]
  (boolean (and
             (re-find #"(?:.*[aeiou].*){3,}" s)
             (re-find #"(\w)(?=\1)" s)
             (nil? (re-find #"ab|cd|pq|xy" s)))))

(defn nice2?
  [s]
  (boolean (and
             (re-find #"(\w{2}).*\1" s)
             (re-find #"(\w).\1" s))))

(defn part1
  []
  (time
    (count (filter nice? input))))

(defn part2
  []
  (time
    (count (filter nice2? input))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= true  (nice? "ugknbfddgicrmopn")))
  (is (= true  (nice? "aaa")))
  (is (= false (nice? "jchzalrnumimnmhp")))
  (is (= false (nice? "haegwjzuvuyypxyu")))
  (is (= false (nice? "dvszwmarrgswjxmb")))
  (is (= true  (nice2? "xyxy")))
  (is (= false (nice2? "aaa")))
  (is (= true  (nice2? "aaabndfaa")))
  (is (= true  (nice2? "qjhvhtzxzqqjkmpb")))
  (is (= true  (nice2? "xxyxx")))
  (is (= false (nice2? "uurcxstgmygtbstg")))
  (is (= false (nice2? "ieodomkazucvgmuy"))))
