(ns abagile.aoc.2023.day03
  (:require
   [abagile.aoc.grid :as grid]
   [abagile.aoc.util :as util]))

(def sample (->> (util/read-input "2023/day03-sample.txt")))
(def input (->> (util/read-input "2023/day03.txt")))

(defn parse
  [s]
  (let [cols (->> s (re-find #".+\n") count dec)
        elms (->> s (re-seq #".") (map first))
        grid (into (sorted-map) (map-indexed #(vector [(quot %1 cols) (rem %1 cols)] %2) elms))]
     (with-meta grid {:dim [(quot (count elms) cols) cols]})))

(defn re-seq-pos
  [re s]
  (let [m (re-matcher re s)]
    ((fn step []
       (when (. m (find))
         (cons [(. m start) (. m end) (. m group)] (lazy-seq (step))))))))

(defn adjacent
  [grid]
  (fn [{[r c1] :start [_ c2] :end :as _num}] 
    (->> (for [offset [-1 1] c (range (dec c1) (+ c2 2))]
           [(+ r offset) c])
         (concat [[r (dec c1)] [r (inc c2)]])
         (filter (grid/bounded grid)))))

;; beware that the regexp matcher start/end position are left inclusive,
;; i.e. start index is at start of match, but end index is 1+ end of match
;; also, when parsing for all numbers, don't strip off all "\n", as otherwise
;; "645\n115" will be parsed as 645115.  In order to rectify this, end index
;; will be `dec` & no. of columns will be `inc` to take into account of the
;; "\n".

(defn part1
  []
  (time
   (let [grid        (parse input)
         [_ cols]    (-> grid meta :dim)
         adjacent-fn (adjacent grid)
         symbols     (-> (into #{} (vals grid)) (disj \.))
         nums        (->> (re-seq-pos #"\d+" input)
                          (mapv (fn [[s e g]]
                                  {:start [(quot s (inc cols)) (rem s (inc cols))]
                                   :end   [(quot (dec e) (inc cols)) (rem (dec e) (inc cols))]
                                   :group (read-string g)})))]
     (->> (for [num nums
                :when (some symbols (map grid (adjacent-fn num)))]
            (:group num))
          (reduce +)))))


(defn part2
  []
  (time 
    (let [grid        (parse input)
          [_ cols]    (-> grid meta :dim)
          adjacent-fn (adjacent grid)
          nums        (->> (re-seq-pos #"\d+" input)
                           (mapv (fn [[s e g]]
                                   {:start [(quot s (inc cols)) (rem s (inc cols))]
                                    :end   [(quot (dec e) (inc cols)) (rem (dec e) (inc cols))]
                                    :group (read-string g)})))]
      (->> (for [num nums
                 :let [nbrs (adjacent-fn num)]
                 :when (some #{\*} (map grid nbrs))]
             [(->> nbrs (filter #(-> % grid #{\*})) first) (:group num)])
           (reduce (fn [m [pt val]] (update m pt conj val)) {})
           (filter #(-> % last count (= 2)))
           (map #(apply * (last %)))
           (reduce +)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
