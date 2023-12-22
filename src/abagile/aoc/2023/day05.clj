(ns abagile.aoc.2023.day05
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def sample (->> (util/read-input-split "2023/day05-sample.txt" #"\n\n")))
(def input (->> (util/read-input-split "2023/day05.txt" #"\n\n")))

(defn parse
  [input]
  (->> input
       (reduce #(let [[cat vals] (cs/split %2 #":")]
                  (assoc %1 (keyword (cs/replace cat #" map$" ""))
                         (cond->
                           (map read-string (re-seq #"\d+" vals))
                           (not= cat "seeds") (->> (partition 3)
                                                   (sort-by second)
                                                   (mapv (fn [[dest src len]] [dest src (dec (+ src len))]))))))
               {})))

(defn mapping
  [m cat]
  (fn [n]
    (-> (for [[d1 s1 s2] (get m cat)
              :when (<= s1 n s2)]
          (+ n (- d1 s1)))
        first
        (or n))))

(defn xform1
  [m n]
  (let [fns (map #(mapping m %) [:humidity-to-location
                                 :temperature-to-humidity
                                 :light-to-temperature
                                 :water-to-light
                                 :fertilizer-to-water
                                 :soil-to-fertilizer
                                 :seed-to-soil])]
    ((apply comp fns) n)))

(defn part1
  []
  (time
    (let [{:keys [seeds] :as m} (parse input)]
      (->> (map (partial xform1 m) seeds) 
           (apply min)))))

(defn map-ranges
  [m cat]
  (fn [src-ranges]
    (->> (reduce (fn [[ranges converted] [d1 s1 s2]]
                   (let [offset (- d1 s1)]
                     (reduce (fn [[a b] [x y]] [(into a x) (into b y)])
                             [[] converted]
                             (for [[n1 n2] ranges]
                               (if (or (<= s1 n1 s2) (<= n1 s1 n2))
                                 (map #(remove (fn [[x y]] (> x y)) %)
                                      [[[(min n1 s1) (dec s1)] [(inc s2) (max n2 s2)]]
                                       [[(+ (max n1 s1) offset) (+ (min n2 s2) offset)]]])
                                 [[[n1 n2]] []])))))
                 [src-ranges []]
                 (get m cat))
         (apply concat))))

(comment
  (def m (parse sample))
  ((map-ranges m :seed-to-soil) [[79 93] [55 67]])
  ((map-ranges m :soil-to-fertilizer) [[57 69] [81 95]])
  ((map-ranges m :fertilizer-to-water) [[57 69] [81 95]])
  ((map-ranges m :water-to-light) [[53 56] [61 69] [81 95]])
  ((map-ranges m :light-to-temperature) [[46 49] [54 62] [74 87] [95 95]])
  ((map-ranges m :temperature-to-humidity) [[45 55] [63 63] [78 80] [82 85] [90 98]])
  ((map-ranges m :humidity-to-location) [[46 56] [64 64] [78 80] [82 85] [90 98]])) 

(defn xform2
  [m src-ranges]
  (let [fns (map #(map-ranges m %) [:humidity-to-location
                                    :temperature-to-humidity
                                    :light-to-temperature
                                    :water-to-light
                                    :fertilizer-to-water
                                    :soil-to-fertilizer
                                    :seed-to-soil])]
    ((apply comp fns) src-ranges)))

(defn part2
  []
  (time
    (let [{:keys [seeds] :as m} (parse input)]
      (->> seeds (partition 2)
           (mapv (fn [[src len]] [src (dec (+ src len))]))
           (xform2 m)
           (map first)
           (apply min)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
