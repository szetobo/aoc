(ns abagile.aoc.2023.day09
  (:require
   [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split "2023/day09.txt" #"\n")
                (mapv #(mapv read-string (re-seq #"[-]?\d+" %)))))

(defn diffs
  [coll]
  (loop [colls [coll]]
    (let [coll' (->> (partition 2 1 (last colls)) (mapv (fn [[x y]] (- y x))))
          colls (conj colls coll')]
      (if (every? zero? coll')
        colls
        (recur colls)))))

(defn append-tail
  [diffs]
  (loop [colls [] diffs diffs]
    (let [item  (or (peek (peek colls)) 0)
          diff  (peek diffs)
          colls (conj colls (conj diff (+ (peek diff) item)))
          diffs (pop diffs)]
      (if (seq diffs)
        (recur colls diffs)
        colls))))

(comment
  (->> (first input) diffs)
  (->> (first input) diffs append-tail))

(defn part1
  []
  (time (->> input
             (map #(->> % diffs append-tail peek peek))
             (reduce +))))


(defn append-head
  [diffs]
  (loop [colls () diffs diffs]
    (let [item  (or (ffirst colls) 0)
          diff  (peek diffs)
          colls (conj colls (conj (seq diff) (- (first diff) item)))
          diffs (pop diffs)]
      (if (seq diffs)
        (recur colls diffs)
        colls))))

(comment
  (->> (first input) diffs)
  (->> (first input) diffs append-head))

(defn part2
  []
  (time (->> input
             (map #(->> % diffs append-head first first))
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
