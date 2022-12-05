(ns abagile.aoc.2022.day05
  (:require
   [clojure.string :as cs]))

(def input
  (let [[crates ops] (-> "resources/2022/day05.txt" slurp (cs/split #"\n\n"))]
    {:stacks (->> (cs/split-lines crates)
                  drop-last
                  (map vec)
                  (map #(map % (range 1 34 4)))
                  (apply map list)
                  (mapv #(remove #{\space} %)))
     :ops (->> (cs/split-lines ops)
               (map #(map read-string (re-seq #"\d+" %))))}))

(defn rearrange
  [move-fn]
  (fn [m [n fr to]]
    (let [[fr to]        (map dec [fr to])
          [crates stack] (split-at n (get m fr))]
      (-> m
          (assoc-in  [fr] stack)
          (update-in [to] move-fn crates)))))

(defn move1
  [stack crates]
  (concat (reverse crates) stack))

(defn part1
  []
  (time (let [{:keys [stacks ops]} input]
          (->> (reduce (rearrange move1) stacks ops)
               (map first)
               (apply str)))))

(defn move2
  [stack crates]
  (concat crates stack))

(defn part2
  []
  (time (let [{:keys [stacks ops]} input]
          (->> (reduce (rearrange move2) stacks ops)
               (map first)
               (apply str)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
