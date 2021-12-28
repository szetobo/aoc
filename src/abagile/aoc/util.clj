(ns abagile.aoc.util
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(def read-input #(slurp (io/resource %)))

(def read-input-split #(cs/split (read-input %1) %2))

(def read-input-split-lines #(cs/split-lines (read-input %)))

(def parse-int (fnil #(Integer/parseInt %) "0"))

(def parse-long (fnil #(Long/parseLong %) "0"))

(def binary-val #(Long/parseLong (apply str %) 2))

(def transpose #(apply map list %))

(def spy #(doto % prn))

(def diff #(if (<= %1 %2) (- %2 %1) (- %1 %2)))

(defn range+
  ([a] (range+ 0 a))
  ([a b]
   (if (<= a b) (range a (inc b)) (range a (dec b) -1))))

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

(defn manhattan-distance
  ([a b] (manhattan-distance (map - a b)))
  ([a] (apply + (map #(Math/abs %) a))))
