set dotenv-load
set quiet
set shell := ["bash", "-cu"]

current_year := `date +%Y`
current_day := `date +%d`

src_dir := "cmd"
inputs_dir := "resources"

alias r := run
alias t := test
alias d := download
alias p := prepare
alias tse := tsedit
alias tsr := tsrun
alias tst := tstest
alias pye := pyedit
alias pyr := pyrun
alias pyt := pytest
alias hye := hyedit
alias hyr := hyrun
alias hyt := hytest

default: run

prepare day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      mkdir -p ./{{src_dir}}/{{year}}/${name}; \
      cp -n ./{{src_dir}}/main.go ./{{src_dir}}/{{year}}/${name}/main.go; \
      cp -n ./ts/template.ts ./ts/{{year}}/${name}.ts; \
      cp -n ./py/template.py ./py/{{year}}/${name}.py; \
      cp -n ./hy/template.hy ./hy/{{year}}/${name}.hy;

download day=current_day year=current_year:
    mkdir -p resources/{{year}}
    name=`printf 'day%02d' $((10#{{day}}))`; \
      day_str=`echo {{day}} | sed 's/^0//'`; \
        curl -sS -b "session=$AOC_SESSION" \
          "https://adventofcode.com/{{year}}/day/${day_str}/input" \
          -o {{inputs_dir}}/{{year}}/${name}.txt

edit day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      $EDITOR -O ./{{src_dir}}/{{year}}/${name}.go {{inputs_dir}}/{{year}}/${name}.txt

run day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      go run ./{{src_dir}}/{{year}}/${name} < {{inputs_dir}}/{{year}}/${name}.txt

test day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      go run ./{{src_dir}}/{{year}}/${name}.ts < {{inputs_dir}}/{{year}}/${name}.sample

tsedit day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      $EDITOR -O ./ts/{{year}}/${name}.ts {{inputs_dir}}/{{year}}/${name}.txt

tsrun day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      bun run ./ts/{{year}}/${name}.ts < {{inputs_dir}}/{{year}}/${name}.txt

tstest day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      bun run ./ts/{{year}}/${name}.ts < {{inputs_dir}}/{{year}}/${name}.sample

pyedit day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      $EDITOR -O ./py/{{year}}/${name}.py {{inputs_dir}}/{{year}}/${name}.txt

pyrun day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      uv run ./py/{{year}}/${name}.py < {{inputs_dir}}/{{year}}/${name}.txt

pytest day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      uv run ./py/{{year}}/${name}.py < {{inputs_dir}}/{{year}}/${name}.sample

hyedit day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      $EDITOR -O ./hy/{{year}}/${name}.hy {{inputs_dir}}/{{year}}/${name}.txt

hyrun day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      uv run hy ./hy/{{year}}/${name}.hy < {{inputs_dir}}/{{year}}/${name}.txt

hytest day=current_day year=current_year:
    name=`printf 'day%02d' $((10#{{day}}))`; \
      uv run hy ./hy/{{year}}/${name}.hy < {{inputs_dir}}/{{year}}/${name}.sample
