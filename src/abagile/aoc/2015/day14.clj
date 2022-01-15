(ns abagile.aoc.2015.day14
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input-split "2015/day14.sample.txt" #"\n"))
(def input  (util/read-input-split "2015/day14.txt" #"\n"))

(defn parse
  [s]
  (reduce #(let [[_ deer & params] (re-find #"(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds." %2)]
             (assoc %1 (keyword deer) (map read-string params)))
    {}
    s))

(defn distance
  [t [speed running resting]]
  (let [[q m] ((juxt quot rem) t (+ running resting))]
    (* (+ (* q running) (min m running)) speed)))

(defn part1
  []
  (time
    (->> (parse input) (util/fmap #(distance 2503 %)) (apply max-key val))))

(defn part2
  []
  (time
    (let [deers (parse input)]
      (->> (reduce (fn [res t]
                     (let [dists    (util/fmap #(distance t %) deers)
                           max-dist (apply max (vals dists))
                           winners  (reduce #(cond-> %1 (= (val %2) max-dist) (conj (key %2))) #{} dists)]
                       (reduce-kv #(assoc %1 %2 (cond-> %3 (contains? winners %2) inc)) res res)))
             (util/fmap (constantly 0) deers)
             (util/range+ 1 2503))
           (apply max-key val)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= {:Comet 1120 :Dancer 1056} (->> (parse sample) (util/fmap #(distance 1000 %))))))
