(ns abagile.aoc.util
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def read-input #(slurp (io/resource %)))

(def read-input-split #(cs/split (read-input %1) %2))

(def read-input-split-lines #(cs/split-lines (read-input %)))

(def parse-int #(if (nil? %) 0 (Integer/parseInt %)))

(def parse-long #(if (nil? %) 0 (Long/parseLong %)))

(def binary-val #(Integer/parseInt (apply str %) 2))

(def transpose #(apply map list %))

(def spy #(doto %  prn))

(def range+ #(if (<= %1 %2) (range %1 (inc %2)) (range %1 (dec %2) -1)))

(def diff #(if (<= %1 %2) (- %2 %1) (- %1 %2)))
