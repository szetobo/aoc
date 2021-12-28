(ns abagile.aoc.2021.day19
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.math.combinatorics :as comb]
    [clojure.set :as set]
    [clojure.string :as cs]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input "2021/day19.sample.txt"))
(def input  (util/read-input "2021/day19.txt"))

;; (defn parse
;;   [s]
;;   (->> (cs/split s #"\n\n") (map #(->> % (re-seq #"-?\d+") rest (map read-string) (partition 3)))))

(defn fingerprint [beacons]
  (->> (comb/combinations beacons 2)
       (map #(apply util/manhattan-distance %))
       frequencies))

(defn parse [s]
  (map (fn [beacons]
         (let [beacons (->> (cs/replace-first beacons #".*\n" "")
                            (re-seq #"-?\d+") (map read-string) (partition 3)
                            set)]
           {:beacons beacons
            :scanners #{[0 0 0]}
            :fingerprint (fingerprint beacons)}))
       (cs/split s #"\n\n")))

(defn fingerprints-match? [f0 f1]
  (<= 66 ; 12 choose 2
      (apply + (keep (fn [[k v0]] (when-let [v1 (f1 k)] (min v0 v1))) f0))))

(defn offset [beacons-0 beacons-1]
  (->> (for [b0 beacons-0 b1 beacons-1] (mapv - b1 b0))
       frequencies
       (some (fn [[diff freq]] (when (<= 12 freq) diff)))))

(def rotations
  (let [rx (fn [[x y z]] [x z (- y)])
        ry (fn [[x y z]] [z y (- x)])
        rz (fn [[x y z]] [y (- x) z])]
    (->> [rx rx rx ry ry ry rz rz rz] comb/subsets
         (map #((apply comp %) [1 2 3]))
         distinct
         (map #(eval
                (list 'fn '[[x y z]]
                      (mapv '{1 x 2 y 3 z -1 (- x) -2 (- y) -3 (- z)} %)))))))

(defn fix [relative fixed]
  (when (fingerprints-match? (:fingerprint relative) (:fingerprint fixed))
    (->> rotations
         (map (fn [rotation]
                (-> relative
                    (update :beacons (fn [b] (set (map rotation b))))
                    (update :scanners (fn [s] (set (map rotation s)))))))
         (some (fn [rotated]
                 (when-let [diff (offset (:beacons rotated) (:beacons fixed))]
                   (-> rotated
                       (update :beacons (fn [b] (set (map #(map + diff %) b))))
                       (update :scanners (fn [s] (set (map #(map + diff %) s)))))))))))

(defn combine [scans]
  (loop [fixed #{(first scans)}
         relative (set (rest scans))]
    (if (seq relative)
      (let [[r r-fixed] (some (fn [f]
                                (some (fn [r]
                                        (when-let [r-fixed (fix r f)]
                                          [r r-fixed]))
                                      relative))
                              fixed)]
        (recur (conj fixed r-fixed) (disj relative r)))
      (apply merge-with set/union (map #(dissoc % :fingerprint) fixed)))))

(defn max-dist [scanners]
  (apply max (for [s0 scanners s1 scanners] (util/manhattan-distance s0 s1))))

(comment
  (count sample)
  (parse sample)
  (count input)
  (parse input))

(defn part1
  []
  (time
    (->> (parse input) combine :beacons count)))

(defn part2
  []
  (time
    (->> (parse input) combine :scanners max-dist)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 79 (count (:beacons (combine (parse sample))))))
  (is (= 3621 (max-dist (:scanners (combine (parse sample)))))))
