(ns abagile.aoc.2020.day12
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def input (->> (util/read-input-split-lines "2020/day12.txt")))

(def sample ["F10" "N3" "F7" "R90" "F11"])

(def abs #(max % (- %)))

(defn move1 [coll]
  (reduce (fn [{dir :dir [x y] :pos :as ctx} [a v]]
            (case a
              "N" (assoc ctx :pos [x (+ y v)])
              "S" (assoc ctx :pos [x (- y v)])
              "E" (assoc ctx :pos [(+ x v) y])
              "W" (assoc ctx :pos [(- x v) y])
              "F" (assoc ctx :pos (condp = dir
                                    0   [(+ x v) y]
                                    90  [x (- y v)]
                                    180 [(- x v) y]
                                    270 [x (+ y v)]
                                    [x y]))
              "L" (assoc ctx :dir (mod (- dir v) 360))
              "R" (assoc ctx :dir (mod (+ dir v) 360))))
          {:dir 0 :pos [0 0]}
          coll))

; rotate L: [10 4] -> [-4 10] -> [-10 -4] -> [4 -10] -> [10 4]
; rotate R: [10 4] -> [4 -10] -> [-10 -4] -> [-4 10] -> [10 4]
;
(defn move2 [coll]
  (reduce (fn [{[wx wy] :wp [x y] :pos :as ctx} [a v]]
            (case a
              "N" (assoc ctx :wp [wx (+ wy v)])
              "S" (assoc ctx :wp [wx (- wy v)])
              "E" (assoc ctx :wp [(+ wx v) wy])
              "W" (assoc ctx :wp [(- wx v) wy])
              "F" (assoc ctx :pos [(+ x (* wx v)) (+ y (* wy v))])
              "L" (assoc ctx :wp (condp = v
                                   90  [(- wy) wx]
                                   180 [(- wx) (- wy)]
                                   270 [wy (- wx)]))
              "R" (assoc ctx :wp (condp = v
                                   90 [wy (- wx)]
                                   180 [(- wx) (- wy)]
                                   270  [(- wy) wx]))))
          {:wp [10 1] :pos [0 0]}
          coll))

(comment
  (->> sample
       (map #(->> % (re-matches #"^([NSEWLRF])(\d+)$") rest vec))
       (map #(-> % (update 1 read-string)))
       move1
       :pos
       (#(let [[x y] %] (+ (abs x) (abs y)))))

  (->> sample
       (map #(->> % (re-matches #"^([NSEWLRF])(\d+)$") rest vec))
       (map #(-> % (update 1 read-string)))
       move2
       :pos
       (#(let [[x y] %] (+ (abs x) (abs y))))))

(defn part1 []
  (->> input
       (map #(->> % (re-matches #"^([NSEWLRF])(\d+)$") rest vec))
       (map #(-> % (update 1 read-string)))
       move1
       :pos
       (#(let [[x y] %] (+ (abs x) (abs y))))))

(defn part2 []
  (->> input
       (map #(->> % (re-matches #"^([NSEWLRF])(\d+)$") rest vec))
       (map #(-> % (update 1 read-string)))
       move2
       :pos
       (#(let [[x y] %] (+ (abs x) (abs y))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
