set dotenv-load
set quiet
set shell := ["bash", "-cu"]

current_year := `date +%Y`
current_day := `date +%d`

src_dir := "cmd"
inputs_dir := "resources"

alias r := run
alias t := testrun
alias d := download
alias p := prepare

run day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      go run ./{{src_dir}}/{{year}}/${name} < {{inputs_dir}}/{{year}}/${name}.txt

testrun day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      go run ./{{src_dir}}/{{year}}/${name} < {{inputs_dir}}/{{year}}/${name}.sample

download day=current_day year=current_year:
    mkdir -p resources/{{year}}
    name=`printf 'day%02d' $((10#{{day}}))`; \
      day_str=`echo {{day}} | sed 's/^0//'`; \
        curl -sS -b "session=$AOC_SESSION" \
          "https://adventofcode.com/{{year}}/day/${day_str}/input" \
          -o {{inputs_dir}}/{{year}}/${name}.txt

prepare day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      mkdir -p ./{{src_dir}}/{{year}}/${name}; \
      cp ./{{src_dir}}/main.go ./{{src_dir}}/{{year}}/${name}/main.go
