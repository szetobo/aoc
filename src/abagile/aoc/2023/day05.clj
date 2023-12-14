(ns abagile.aoc.2023.day05
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def input (->> (util/read-input-split "2023/day05.txt" #"\n\n")
                (reduce #(let [[cat vals] (cs/split %2 #":")]
                           (assoc %1 cat
                                  (cond->>
                                    (map read-string (re-seq #"\d+" vals))
                                    (not= cat "seeds") (partition 3))))
                        {})))

(defn mapping
  [cat n]
  (-> (for [[dest src len] (get input cat)
            :when (<= src n (+ src (dec len)))
            :let [offset (- n src)]]
                  ;; _ (prn src n (+ src (dec len)))
                  ;; _ (prn dest offset (+ dest offset))]]
        (+ dest offset))
      first
      (or n)))

(defn part1
  []
  (time (apply min (map #(->> %
                              (mapping "seed-to-soil map")
                              (mapping "soil-to-fertilizer map")
                              (mapping "fertilizer-to-water map")
                              (mapping "water-to-light map")
                              (mapping "light-to-temperature map")
                              (mapping "temperature-to-humidity map")
                              (mapping "humidity-to-location map"))
                        (get input "seeds")))))

(defn part2
  []
  (time (->> (get input "seeds") (partition 2))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
