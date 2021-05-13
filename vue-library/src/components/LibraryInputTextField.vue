<template>
  <div class="row mb-18">
    <div class="input-field col s9">

      <template v-if="possibleValuesList != null && type != null">
        <input :id="nameToShow" :type="type" class="validate mb-18" v-model="value" @focusout="input" :list="'possible'+ nameToShow" >
        <datalist :id="'possible'+ nameToShow">
          <option v-for="possibleValue in possibleValuesList" :key="possibleValue" :value="possibleValue" />
        </datalist>
        <label :for="nameToShow">{{ nameToShow }}</label>
        <span class="helper-text mb-18" data-error="wrong" data-success="right"></span>
      </template>

      <template v-else-if="type != null">
        <input :id="nameToShow" :type="type" class="validate mb-18" v-model="value" @focusout="input">
        <label :for="nameToShow">{{ nameToShow }}</label>
        <span class="helper-text mb-18" data-error="wrong" data-success="right"></span>
      </template>

    </div>
  </div>
</template>

<script>
export default {
  name: "LibraryInputTextField",
  props: { nameToShow: String, nameInBackend: String, type: String, possibleValuesList: Array, urlToFindMappedValue: String },
  methods: {
    async input() {
      let valueForResponse = ''
      if (this.urlToFindMappedValue) {
        let possibleInputValues = document.querySelector('#possible'+ this.nameToShow).getElementsByTagName('option')
        let isPossible = false
        for (let possibleInputValue of possibleInputValues) {
          if (this.value === possibleInputValue.getAttribute('value')) {
            isPossible = true
            break
          }
        }
        if (isPossible) {
          let urlArguments = ''
          this.value.split(',').forEach(argument => urlArguments += '/' + argument)
          const mappedValueResponse = await fetch('http://localhost:8081/lib' + this.urlToFindMappedValue + urlArguments, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json'
            }
          });
          if (mappedValueResponse.ok) {
            valueForResponse = await mappedValueResponse.json()
          }
        } else {
          document.querySelector('#' + this.nameToShow).classList.add('invalid')
        }
      }
      else {
        valueForResponse = this.value
      }
      this.$emit('input', {nameInBackend: this.nameInBackend, value: valueForResponse})
    }
  },
  data() {
    return {
      value: '',
    }
  }
}

</script>

<style scoped>
.mb-18 {
  margin-bottom: -18%;
}
</style>