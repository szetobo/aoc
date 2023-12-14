(ns abagile.aoc.2023.day08
  (:require
   [abagile.aoc.util :as util]))

(def input (let [[inst routes] (util/read-input-split "2023/day08.txt" #"\n\n")]
             {:inst   inst
              :routes (->> (partition 3 (re-seq #"\w{3}" routes))
                           (reduce (fn [m [x y z]] (assoc m x [y z])) {}))}))

(defn solve
  [start end-re]
  (let [{:keys [inst routes]} input]
       (loop [insts (cycle (seq inst)) prev start steps 1]
         (let [curr  (get-in routes [prev (if (= (first insts) \L) 0 1)])]
           (if (re-find end-re curr)
             steps
             (recur (next insts) curr (inc steps)))))))


(defn part1
  []
  (time (solve "AAA" #"^ZZZ$")))


(defn part2
  []
  (time (let [starts (->> input :routes keys (filter #(re-find #"A$" %)))
              steps (map #(solve % #"Z$") starts)
              gcd   (reduce #(.gcd %1 %2) (map biginteger steps))]
          (->> (map #(/ % gcd) steps)
               (reduce *)
               (* gcd)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
