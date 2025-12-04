set dotenv-load
set quiet

current_year := `date +%Y`
current_day := `date +%d`

src_dir := "cmd"
inputs_dir := "resources"

alias r := run
alias t := testrun
alias d := download

run day=current_day year=current_year:
    fname=`printf 'day%02d' {{day}}`; \
      go run ./{{src_dir}}/{{year}}/${fname} < {{inputs_dir}}/{{year}}/${fname}.txt

testrun day=current_day year=current_year:
    fname=`printf 'day%02d' {{day}}`; \
      go run ./{{src_dir}}/{{year}}/${fname} < {{inputs_dir}}/{{year}}/${fname}.sample

download day=current_day year=current_year:
    mkdir -p resources/{{year}}
    fname=`printf 'day%02d' {{day}}`; \
      day_str=`echo {{day}} | sed 's/^0//'`; \
        curl -sS -b "session=$AOC_SESSION" \
          "https://adventofcode.com/{{year}}/day/${day_str}/input" \
          -o {{inputs_dir}}/{{year}}/${fname}.txt
