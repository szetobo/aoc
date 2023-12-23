(ns abagile.aoc.2023.day12
  (:require
   [abagile.aoc.util :as util]
   [clojure.string :as cs]))

(def input (->> (util/read-input-split "2023/day12.txt" #"\n")))

(defn parse
  [input]
  (->> input
       (map #(-> %
                 (cs/split #" ")
                 (as-> $ (let [[form1 form2] $]
                           [form1 (map read-string (cs/split form2 #","))]))))))

(defn ways-dp
  [spring groups]
  (let [spring (str \. spring \.)
        groups (-> (mapcat #(into [\.] (repeat % \#)) groups) (concat [\.]) vec)
        [n m]  (map count [spring groups])
        dp     (-> (vec (repeat (inc n) (vec (repeat (inc m) 0))))
                   (assoc-in [n m] 1))]
    (->> (loop [dp dp row (dec n) col (dec m)]
           (if (neg? row) dp
             (let [[row' col'] (if (> col (max (- m (- n row)) 0)) [row (dec col)] [(dec row) (dec m)])
                   [ch g-ch]   [(get spring row) (get groups col)]
                   sum         (case ch
                                 \# (if (= g-ch \#) (get-in dp [(inc row) (inc col)]) 0)
                                 \. (if (= g-ch \.)
                                      (+ (get-in dp [(inc row) (inc col)])
                                         (get-in dp [(inc row) col]))
                                      0)
                                 (if (= g-ch \#)
                                   (get-in dp [(inc row) (inc col)])
                                   (+ (get-in dp [(inc row) (inc col)])
                                      (get-in dp [(inc row) col]))))]
               (recur (assoc-in dp [row col] sum) row' col'))))
         ffirst)))

(def ways
  (memoize
   (fn [spring groups]
    (cond
      (empty? spring) (if (empty? groups) 1 0)
      (empty? groups) (if (some #{\#} spring) 0 1)
      :else
      (let [fg (first groups)
            [ch s-len] ((juxt first count) spring)]
        (cond-> 0
          (#{\. \?} ch)
          (+ (ways (next spring) groups))

          (and (#{\# \?} ch)
               (<= fg s-len)
               (not (some #{\.} (take fg spring)))
               (or (= fg s-len) (not= (nth spring fg) \#)))
          (+ (ways (drop (inc fg) spring) (next groups)))))))))

(comment
  (apply ways ["???.###" [1 1 3]])
  (apply ways [".??..??...?##." [1 1 3]])
  (apply ways-dp ["???.###" [1 1 3]])
  (apply ways-dp [".??..??...?##." [1 1 3]])
  (apply ways-dp ["?###????????" [3 2 1]]))

(defn part1
  []
  (time (->> input parse
             (map #(apply ways %)) (reduce +))))

(defn part2
  []
  (time (->> input parse
             (map (fn [[s g]] [(cs/join "?" (repeat 5 s)) (flatten (repeat 5 g))]))
             (map #(apply ways %)) (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
