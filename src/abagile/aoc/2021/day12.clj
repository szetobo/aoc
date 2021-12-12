(ns abagile.aoc.2021.day12
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample1 (util/read-input "2021/day12.sample1.txt"))
(def sample2 (util/read-input "2021/day12.sample2.txt"))
(def sample3 (util/read-input "2021/day12.sample3.txt"))

(def input (util/read-input "2021/day12.txt"))

(defn parse
  [s]
  (->> s (re-seq #"[^-\n]+") (partition 2)
       (reduce (fn [g [a b]]
                 (-> g
                     (update a (fnil conj #{}) b)
                     (update b (fnil conj #{}) a)))
         {})))

(defn find-paths*
  "Depth first search to find all paths to a goal"
  [filter-node-fn start dest graph]
  (letfn [(search [path visited]
            (let [curr (peek path)]
              (if (= dest curr)
                [path]
                (->> (get graph curr)
                     (filter #(filter-node-fn visited start dest %))
                     (mapcat #(search (conj path %) (update visited % (fnil inc 0))))))))]
    (search [start] {start 1})))

(def find-paths1 (partial find-paths* (fn [visited _start _dest node] (or (re-matches #"[A-Z]+" node) (nil? (get visited node))))))
(def find-paths2 (partial find-paths* (fn [visited start dest node]
                                        (cond
                                          (re-matches #"[A-Z]+" node) true
                                          (#{start dest} node)         (nil? (get visited node))
                                          :else (or (nil? (get visited node))
                                                  (->> visited
                                                       (filter #(re-matches #"[a-z]+" (key %)))
                                                       (every? #(= (val %) 1))))))))

(comment
  (count sample1)
  (count sample2)
  (count sample3)
  (->> (parse sample1) (find-paths1 "start" "end") count)
  (->> (parse sample2) (find-paths1 "start" "end") count)
  (->> (parse sample3) (find-paths1 "start" "end") count)
  (->> (parse sample1) (find-paths2 "start" "end") count)
  (->> (parse sample2) (find-paths2 "start" "end") count)
  (->> (parse sample3) (find-paths2 "start" "end") count)
  (count input))

(defn part1
  []
  (time (->> (parse input) (find-paths1 "start" "end") count)))

(defn part2
  []
  (time (->> (parse input) (find-paths2 "start" "end") count)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
