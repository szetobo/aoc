(ns abagile.aoc.2020.day24
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest run-tests is]]))

(def input (->> (util/read-input-split-lines "2020/day24.txt")))

(def sample ["sesenwnenenewseeswwswswwnenewsewsw"
             "neeenesenwnwwswnenewnwwsewnenwseswesw"
             "seswneswswsenwwnwse"
             "nwnwneseeswswnenewneswwnewseswneseene"
             "swweswneswnenwsewnwneneseenw"
             "eesenwseswswnenwswnwnwsewwnwsene"
             "sewnenenenesenwsewnenwwwse"
             "wenwwweseeeweswwwnwwe"
             "wsweesenenewnwwnwsenewsenwwsesesenwne"
             "neeswseenwwswnwswswnw"
             "nenwswwsewswnenenewsenwsenwnesesenew"
             "enewnwewneswsewnwswenweswnenwsenwsw"
             "sweneswneswneneenwnewenewwneswswnese"
             "swwesenesewenwneswnwwneseswwne"
             "enesenwswwswneneswsenwnewswseenwsese"
             "wnwnesenesenenwwnenwsewesewsesesew"
             "nenewswnwewswnenesenwnesewesw"
             "eneswnwswnwsenenwnwnwwseeswneewsenese"
             "neswnwewnwnwseenwseesewsenwsweewe"
             "wseweeenwnesenwwwswnew"])

(defn parse [steps]
  (->> (map #(re-seq #"(?:se|sw|ne|nw|e|w)+?" %) steps)
       (map #(reduce (fn [[x y] dir] (case dir
                                       "e"  [(inc x) y]
                                       "w"  [(dec x) y]
                                       "se" [x (dec y)]
                                       "sw" [(dec x) (dec y)]
                                       "ne" [(inc x) (inc y)]
                                       "nw" [x (inc y)]))
                    [0 0] %))))

(defn flip1 [steps]
  (->> (parse steps)
       (reduce (fn [res tiles]
                 (if (res tiles)
                   (disj res tiles)
                   (conj res tiles)))
               #{})))

(defn adjacents [x y]
  [[(inc x) y] [(dec x) y] [x (dec y)] [(dec x) (dec y)] [(inc x) (inc y)] [x (inc y)]])

(defn count-of-adjacents [data x y]
  (->> (map data (adjacents x y))
       (filter some?)
       count))

(defn flip2 [data]
  (let [xs (- (->> (map first data) (apply min)) 1)
        xl (+ (->> (map first data) (apply max)) 2)
        ys (- (->> (map second data) (apply min)) 1)
        yl (+ (->> (map second data) (apply max)) 2)]
    (->> (for [x (range xs xl)
               y (range ys yl)
               :let [black? (data [x y])
                     counts (count-of-adjacents data x y)]
               :when (if black?
                       (and (<= 1 counts 2))
                       (= counts 2))]
           [x y])
         set)))

(defn part1 []
  (time (count (flip1 input))))

(defn part2 []
  (time (count (first (drop 100 (iterate flip2 (flip1 input)))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (let [data (flip1 sample)]
    (is (= (parse ["esew" "nwwswee"]) [[0 -1] [0 0]]))
    (is (= (count data) 10))
    (is (= data #{[0 0] [0 -2] [-3 -3] [3 3] [-3 -2]
                  [-2 -1] [-1 1] [2 0] [-2 0] [0 1]}))
    (is (= (adjacents 0 0) [[1 0] [-1 0] [0 -1] [-1 -1] [1 1] [0 1]]))
    (is (= (flip2 data) #{[0 0] [1 0] [-3 -3] [1 1] [-3 -2]
                          [-1 -1] [-2 1] [-4 -3] [-2 -1] [1 -1]
                          [0 2] [-1 1] [0 -1] [-2 0] [0 1]}))
    (is (= (count (first (drop 100 (iterate flip2 (flip1 sample))))) 2208))))

(deftest test-input
  (is (= (count (flip1 input)) 277))
  (is (= (count (first (drop 100 (iterate flip2 (flip1 input))))) 3531)))

(run-tests)
