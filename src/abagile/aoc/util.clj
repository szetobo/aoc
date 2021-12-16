(ns abagile.aoc.util
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def read-input #(slurp (io/resource %)))

(def read-input-split #(cs/split (read-input %1) %2))

(def read-input-split-lines #(cs/split-lines (read-input %)))

(def parse-int (fnil #(Integer/parseInt %) "0"))

(def parse-long (fnil #(Long/parseLong %) "0"))

(def binary-val #(Integer/parseInt (apply str %) 2))

(def transpose #(apply map list %))

(def spy #(doto % prn))

(def range+ #(if (<= %1 %2) (range %1 (inc %2)) (range %1 (dec %2) -1)))

(def diff #(if (<= %1 %2) (- %2 %1) (- %1 %2)))

(defn fmap
  [f m]
  (reduce-kv #(assoc %1 %2 (f %3)) {} m))

(defn fmap-kv
  [f m]
  (reduce-kv #(assoc %1 %2 (f %2 %3)) {} m))

(defn fmap-keys
  [f m]
  (reduce-kv #(assoc %1 (f %2) %3) {} m))

(comment
  (fmap str {:a 1 :b 2 :c 3})         ; {:a "1", :b "2", :c "3"}
  (fmap-keys name {:a 1 :b 2 :c 3}))  ; {"a" 1, "b" 2, "c" 3}

(defn remove-keys
  [f m]
  (select-keys m (filter (complement f) (keys m))))
