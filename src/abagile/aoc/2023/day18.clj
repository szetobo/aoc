(ns abagile.aoc.2023.day18
  (:require
   [abagile.aoc.util :as util]
   [clojure.test :refer [deftest is]]))

(def sample (->> (util/read-input-split-lines "2023/day18-sample.txt")))
(def input (->> (util/read-input-split-lines "2023/day18.txt")))

(defn parse
  [input]
  (->> input (map #(->> %
                        (re-seq #"(U|D|L|R) (\d+) \((.+)\)")
                        (mapcat rest)
                        ((fn [[x y z]] [(keyword x) (read-string y) z]))))))

;; The therom behind this calculation is Shoelace Formula & Pick's Theorem
;; - https://en.wikipedia.org/wiki/Shoelace_formula
;; - https://en.wikipedia.org/wiki/Pick%27s_theorem
(defn cal-filled
  [plan]
  (let [pts  (reduce (fn [pts [dir step]]
                       (let [[row col] (peek pts)]
                         (conj pts (case dir
                                     :U [(- row step) col]
                                     :D [(+ row step) col]
                                     :L [row (- col step)]
                                     :R [row (+ col step)]))))
                     [[0 0]] plan)
        A (-> (reduce + (for [i (range (dec (count pts)))
                              :let [[x1 y1 x2 y2] (map #(bigint (get-in pts %)) [[i 0] [i 1] [(inc i) 0] [(inc i) 1]])]]
                          (- (* x1 y2) (* x2 y1))))
              (/ 2)
              util/abs)
        b (->> plan (map second) (apply +))
        i (inc (- A (/ b 2)))]
    (+ i b)))

(deftest test1
  (is (= 1 1)))

(defn part1
  []
  (time
   (->> input parse cal-filled)))


(defn parse2
  [input]
  (->> input (map #(->> %
                       (re-seq #"(U|D|L|R) \d+ \((.+)\)")
                       (mapcat last) (drop 1) ((juxt butlast last))
                       ((fn [[x y]] [({\0 :R \1 :D \2 :L \3 :U} y) (read-string (str "0x" (apply str x)))]))))))

(deftest test2
  (is (= 1 1)))

(defn part2
  []
  (time
   (->> input parse2 cal-filled)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
