(ns abagile.aoc.2015.day8
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(defn input [] (cs/split-lines (slurp (io/resource "2015/day8.txt"))))

(comment
  (count (input)))

(def code-chars (comp count seq))
(def decode-diff #(+ 2
                     (if-let [n (re-seq #"\\\"|\\\\" %)]
                       (count n)
                       0)
                     (if-let [n (re-seq #"\\x[\da-f]{2}" %)]
                       (* 3 (count n))
                       0)))

(->> (input)
     ; (take 5)
     (map decode-diff)
     (reduce +))

(comment
  (code-chars "\"yr\\\\bajyndte\\\\rm\"")
  (count (read-string "\"yr\\\\bajyndte\\\\rm\""))
  (decode-diff "\"yr\\\\bajyndte\\\\rm\"")
  (re-seq #"\\\"|\\\\" "\"yr\\\\bajyndte\\\\rm\"")
  (cs/split "aa\"aa" #"\""))

(def encode-diff #(+ 4
                     (if-let [n (re-seq #"\\\"|\\\\" %)]
                       (* 2 (count n))
                       0)
                     (if-let [n (re-seq #"\\x[\da-f]{2}" %)]
                       (* (count n))
                       0)))

(->> (input)
     ; (take 5)
     (map encode-diff)
     (reduce +))
