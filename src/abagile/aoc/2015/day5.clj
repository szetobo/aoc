(ns abagile.aoc.2015.day5
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(defn input [] (cs/split-lines (slurp (io/resource "2015/day5.txt"))))

(comment
  (count (input)))

(defn valid [s]
  (boolean (and
             (re-find #"(?:.*[aeiou].*){3,}" s)
             (re-find #"(\w)(?=\1)" s)
             (nil? (re-find #"ab|cd|pq|xy" s)))))

(comment
  (valid "ugknbfddgicrmopn")
  (valid "aaa")
  (valid "jchzalrnumimnmhp")
  (valid "haegwjzuvuyypxyu")
  (valid "dvszwmarrgswjxmb")

  (->> (input)
       (filter valid)
       count))

(defn valid2 [s]
  (boolean (and
             (re-find #"(\w{2}).*\1" s)
             (re-find #"(\w).\1" s))))

(comment
  (valid2 "xyxy")
  (valid2 "aaa")
  (valid2 "aaabndfaa")
  (valid2 "qjhvhtzxzqqjkmpb")
  (valid2 "xxyxx")
  (valid2 "uurcxstgmygtbstg")
  (valid2 "ieodomkazucvgmuy")

  (->> (input)
       (filter valid2)
       count))
