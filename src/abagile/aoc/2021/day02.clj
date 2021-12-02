(ns abagile.aoc.2021.day02
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]))

(def sample-input [[:forward 5]
                   [:down 5]
                   [:forward 8]
                   [:up 3]
                   [:down 8]
                   [:forward 2]])

(def input (->> (util/read-input-split-lines "2021/day02.txt")
                (map #(re-matches #"(forward|down|up) (\d+)" %))
                (map (fn [[_ k v]] [(keyword k) (util/parse-int v)]))))

(comment
  (count input)
  (take 10 input)
  (->> (filter #(= (first %) :forward) input) (map second) (apply +))
  (->> (filter #(= (first %) :up) input) (map second) (apply +))
  (->> (filter #(= (first %) :down) input) (map second) (apply +)))

(defn move
  [{:keys [x y] :or {x 0 y 0}} [dir unit]]
  {:x (cond-> x
        (= dir :forward) (+ unit))
   :y (cond-> y
        (= dir :down) (+ unit)
        (= dir :up)   (- unit))})

(defn move'
  [[x y] [dir unit]]
  (case dir
    :forward [(+ x unit) y]
    :down    [x (+ y unit)]
    :up      [x (- y unit)]))

(defn move''
  [{:keys [x y] :or {x 0 y 0}} [dir unit]]
  (case dir
    :forward {:x (+ x unit) :y y}
    :down    {:x x          :y (+ y unit)}
    :up      {:x x          :y (- y unit)}))

(comment
  (move {} [:forward 1])
  (move {} [:down 1])
  (reduce move {} sample-input)
  (move' [0 0] [:forward 1])
  (reduce move' [0 0] sample-input)
  (move'' {} [:forward 1])
  (reduce move'' {} sample-input))

(defn part1
  []
  (time (let [{:keys [x y]} (reduce move {} input)]
          (* x y))))

(defn move2
  [{:keys [x y aim] :or {x 0 y 0 aim 0}} [dir unit]]
  {:x   (cond-> x
          (= dir :forward) (+ unit))
   :y   (cond-> y
          (= dir :forward) (+ (* aim unit)))
   :aim (cond-> aim
          (= dir :down) (+ unit)
          (= dir :up)   (- unit))})

(defn move2''
  [{:keys [x y aim] :or {x 0 y 0 aim 0}} [dir unit]]
  (case dir
    :forward {:x (+ x unit) :y (+ y (* aim unit)) :aim aim}
    :down    {:x x          :y y                  :aim (+ aim unit)}
    :up      {:x x          :y y                  :aim (- aim unit)}))

(comment
  (move2 {} [:forward 1])
  (move2 {} [:down 1])
  (reduce move2 {} sample-input)
  (reduce move2'' {} sample-input))

(defn part2
  []
  (time (let [{:keys [x y]} (reduce move2'' {} input)]
          (* x y))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(defrecord Loc
  [hr dp aim cmd])

(defn make-loc
  [{:keys [hr dp aim cmd] :or {hr 0 dp 0 aim 0 cmd []} :as params}]
  (map->Loc (assoc params :hr hr :dp dp :aim aim :cmd cmd)))

(defn move-loc
  [loc [dir X :as cmd]]
  (let [loc' (assoc loc :cmd cmd)]
    (case dir
      :forward (make-loc (update loc' :hr + X))
      :down    (make-loc (update loc' :dp + X))
      :up      (make-loc (update loc' :dp - X)))))

(comment
  (-> (make-loc {})
      (move-loc [:forward 1])
      (move-loc [:down 5])
      (move-loc [:up 3]))
  (reductions move-loc (make-loc {}) sample-input)
  (take 10 (reductions move-loc (make-loc {}) input))
  (time (reduce move-loc (make-loc {}) input)))
