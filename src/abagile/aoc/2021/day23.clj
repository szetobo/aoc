(ns abagile.aoc.2021.day23
  (:gen-class)
  (:require
    [abagile.aoc.algo :as algo]
    [abagile.aoc.ocr :as ocr]
    [abagile.aoc.util :as util]
    [clojure.string :as cs]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input "2021/day23.sample.txt"))
(def input  (util/read-input "2021/day23.txt"))

(defn parse [s]
  (->> (ocr/parse s {"#" \# "." nil "A" \A "B" \B "C" \C "D" \D " " \space})
       (remove (comp #{\# \space} val))
       (into (sorted-map))))

(defn unfold
  [s]
  (let [[top bottom] (->> s cs/split-lines (split-at 3))]
    (cs/join "\n" (concat top ["  #D#C#B#A#  " "  #D#B#A#C#  "] bottom))))

(def halls #{[1 1] [1 2] [1 4] [1 6] [1 8] [1 10] [1 11]})

(defn rooms
  [grid amphipod]
  (->> (keys grid)
       (filter (fn [[y x]] (and (< 1 y) (= (case amphipod \A 3 \B 5 \C 7 \D 9) x))))
       (sort #(compare %2 %1))))

(def step-energy {\A 1 \B 10 \C 100 \D 1000})

(defn path
  [hall room]
  (let [[[hall-y hall-x] [room-y room-x]] (sort [hall room])]
    (concat (map (fn [x] [hall-y x]) (util/range+ hall-x room-x))
            (map (fn [y] [y room-x]) (util/range+ 2 room-y)))))

(defn clear? [grid paths]
  (= 1 (count (keep grid paths))))

(defn vacancy [grid amphipod]
  (when (->> amphipod (rooms grid) (keep grid) (every? #{amphipod}))
    (->> amphipod (rooms grid) (remove grid) first)))

(defn all-home? [grid amphipod]
  (every? #(= amphipod (grid %)) (rooms grid amphipod)))

(defn move [grid amphipod hall room]
  (let [paths (path hall room)]
    (when (clear? grid paths)
      [(assoc grid hall nil room amphipod)
       (* (get step-energy amphipod) (dec (count paths)))])))

(defn moves [grid]
  (into {} (apply concat
             (keep (fn [hall]
                     (when-let [amphipod (grid hall)]
                       (when-let [room (vacancy grid amphipod)]
                         (move grid amphipod hall room))))
               halls)
             (keep (fn [room]
                     (when-let [amphipod (grid room)]
                       (keep (fn [hall] (move grid amphipod room hall)) halls)))
               (mapcat #(rooms grid %) (remove #(all-home? grid %) "ABCD"))))))

(comment
  (count sample)
  (-> (parse sample) (rooms \A))
  (-> (parse sample) (vacancy \A))
  (-> (parse sample) (all-home? \A))
  (path [1 10] [3 3])
  (move (parse sample) \B [1 1] [2 3])
  (moves (parse sample))
  (count input)
  (parse (unfold input)))

(defn part1
  []
  (time
    (->> (algo/dijkstra (parse input) moves)
         (filter (fn [[grid _]] (every? #(all-home? grid %) "ABCD")))
         first second)))

(defn part2
  []
  (time
    (->> (algo/dijkstra (parse (unfold input)) moves)
         (filter (fn [[grid _]] (every? #(all-home? grid %) "ABCD")))
         first second)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= 0 0)))
