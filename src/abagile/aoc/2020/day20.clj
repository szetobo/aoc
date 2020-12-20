(ns abagile.aoc.2020.day20
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]
    [clojure.test :refer [deftest run-tests is]]))

(def input (->> (util/read-input-split "2020/day20.txt" #"\n\n")
               (map cs/split-lines)))

(def sample (->> (util/read-input-split "2020/day20-sample.txt" #"\n\n")
                 (map cs/split-lines)))

(def parse #(let [[title & body] %
                  tid (util/parse-int (re-find #"\d+" title))]
              {tid (->> body (map seq) (map (fn [x] (map {\# 1 \. 0} x))))}))

(def build-map #(->> (map parse %) (reduce merge {})))

(def rotate-cw #(->> (apply interleave %)
                     (partition 10)
                     (map reverse)))

(def rotate-ccw #(->> (map reverse %)
                      (apply interleave)
                      (partition 10)))

(def flip-h #(map reverse %))

(def flip-v reverse)

(def flip-cw (comp flip-v rotate-cw))

(def flip-ccw (comp flip-v rotate-ccw))

(defn edges [x]
  (list (-> x first)
        (-> x first reverse)
        (-> x rotate-ccw first)
        (-> x rotate-ccw first reverse)
        (-> x rotate-ccw rotate-ccw first)
        (-> x rotate-ccw rotate-ccw first reverse)
        (-> x rotate-cw first)
        (-> x rotate-cw first reverse)))

; (defn connect-with [x y]
;   (for [[xi xe] (map-indexed vector (edges x))
;         [yi ye] (map-indexed vector (edges y))
;         :when (= xe ye)]
;     [xi yi]))

; (connect-with ((build-map sample) 1951) ((build-map sample) 2311))
; (connect-with ((build-map sample) 1951) ((build-map sample) 2729))
; (connect-with ((build-map sample) 3079) ((build-map sample) 2311))
; (connect-with ((build-map sample) 3079) ((build-map sample) 2473))
; (connect-with ((build-map sample) 2971) ((build-map sample) 2729))
; (connect-with ((build-map sample) 2971) ((build-map sample) 1489))
; (connect-with ((build-map sample) 1171) ((build-map sample) 2473))
; (connect-with ((build-map sample) 1171) ((build-map sample) 1489))

; ([2 7] [3 6])
; ([0 5] [1 4])
; ([2 7] [3 6])
; ([0 5] [1 4])
; ([6 2] [7 3])
; ([4 2] [5 3])
; ([4 1] [5 0])
; ([2 3] [3 2])

(defn connect? [x y]
  (boolean (seq (for [[xi xe] (map-indexed vector (edges x))
                      [yi ye] (map-indexed vector (edges y))
                      :when (= xe ye)]
                  [xi yi]))))

(defn corners [m]
  (->> (for [x (sort (keys m))
             y (sort (keys m))
             :when (not= x y)
             :when (connect? (m x) (m y))]
          [x y])
       (partition-by first)
       (filter #(= (count %) 2))))

(defn part1 []
  (time (->> (corners (build-map input))
             (map ffirst)
             (reduce *))))

(defn part2 [])

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-parse-sample
  (let [m (build-map sample)]
    (is (= (->> m count) 9))
    (is (= (->> m vals (map count) distinct) [10]))
    (is (= (->> m vals (mapcat identity) (map count) distinct) [10]))
    (is (= (->> (m 1951) rotate-ccw) [[0 1 1 1 1 1 0 0 1 0]
                                      [1 0 1 1 0 1 1 1 0 0]
                                      [1 0 0 1 0 1 1 0 1 1]
                                      [0 0 0 1 0 1 0 0 0 0]
                                      [0 1 1 1 0 1 1 0 0 1]
                                      [0 1 0 1 1 0 1 0 1 1]
                                      [1 1 0 0 0 1 0 1 0 0]
                                      [1 1 0 0 1 1 1 1 1 0]
                                      [0 0 0 0 1 1 1 1 0 0]
                                      [1 1 0 1 0 0 1 0 0 1]]))
    (is (= (->> (m 1951) edges) [[1 0 1 1 0 0 0 1 1 0]
                                 [0 1 1 0 0 0 1 1 0 1]
                                 [0 1 1 1 1 1 0 0 1 0]
                                 [0 1 0 0 1 1 1 1 1 0]
                                 [0 0 1 0 1 1 0 0 0 1]
                                 [1 0 0 0 1 1 0 1 0 0]
                                 [1 0 0 1 0 0 1 0 1 1]
                                 [1 1 0 1 0 0 1 0 0 1]]))
    (is (= (connect? (m 1951) (m 2311)) true))
    (is (= (connect? (m 1951) (m 2729)) true))
    (is (= (connect? (m 1951) (m 2311)) true))
    (is (= (connect? (m 1951) (m 1427)) false))
    (is (= (connect? (m 1951) (m 3079)) false))
    (is (= (connect? (m 1951) (m 2473)) false))
    (is (= (corners m) [[[1171 1489] [1171 2473]] [[1951 2311] [1951 2729]] [[2971 1489] [2971 2729]] [[3079 2311] [3079 2473]]]))))

(deftest test-parse-input
  (let [m (build-map input)]
    (is (= (->> m count) 144))
    (is (= (->> m vals (map count) distinct) [10]))
    (is (= (->> m vals (mapcat identity) (map count) distinct) [10]))))

(run-tests)
