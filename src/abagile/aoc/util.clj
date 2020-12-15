(ns abagile.aoc.util
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def read-input #(slurp (io/resource %)))

(def read-input-split #(cs/split (read-input %1) %2))

(def read-input-split-lines #(cs/split-lines (read-input %)))

(def parse-int #(if (nil? %) 0 (Integer/parseInt %)))

(def parse-long #(if (nil? %) 0 (Long/parseLong %)))

(defn binary-val [coll]
  (reduce #(+ (* %1 2) (- (int %2) (int \0))) 0 coll))
