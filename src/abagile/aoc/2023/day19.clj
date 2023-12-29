(ns abagile.aoc.2023.day19
  (:require
   [abagile.aoc.util :as util]
   [clojure.set :as set]
   [clojure.string :as cs]
   [clojure.test :refer [deftest is]]))


(def sample (->> (util/read-input-split "2023/day19-sample.txt" #"\n\n")
                 (map #(cs/split % #"\n"))))
(def input (->> (util/read-input-split "2023/day19.txt" #"\n\n")
                (map #(cs/split % #"\n"))))

(def p1 (first sample))
(def p2 (second sample))

(defn parse
  [[p1 p2]]
  (let [workflows (->> (map #(-> % (cs/escape {\} ""}) (cs/split #"\{")) p1)
                       (reduce (fn [m [n r]]
                                 (let [rules (cs/split r #",")]
                                   (assoc m (read-string n) (conj (->> (butlast rules)
                                                                       (mapcat #(map rest (re-seq #"([^<>]+)([<>])(\d+)\:(\w+)" %)))
                                                                       (mapv (partial map read-string)))
                                                                  (list (read-string (last rules)))))))
                               {}))
        parts (->> (map #(-> % (cs/escape {\{ "" \} ""})) p2)
                   (map #(map rest (re-seq #"([xmas])=(\d+)" %)))
                   (map #(reduce (fn [m [k v]] (assoc m (read-string k) (read-string v))) {} %)))]
    [workflows parts]))

(defn check
  [wfs part]
  (loop [rules (wfs 'in)]
     (let [rule (first rules)
           [p o n wf] rule
           wf (if (nil? o) p wf)]
       (if (or (nil? o) (({'> > '< <} o) (get part p) n))
         (if (#{'A 'R} wf) wf
           (recur (get wfs wf)))
         (recur (next rules))))))

(deftest test1
  (is (= 1 1)))

(defn part1
  []
  (time
   (let [[wfs parts] (parse input)]
     (->> (map #(if (= %2 'R) 0 (apply + (vals %1)))
               parts
               (map (partial check wfs) parts))
          (reduce +)))))


(deftest test2
  (is (= 1 1)))

(defn part2
  []
  (time
   1))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
