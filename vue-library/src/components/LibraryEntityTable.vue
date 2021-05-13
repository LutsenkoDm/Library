<template>
  <div class="col s9">
    <table class="responsive-table highlight">

      <thead>
        <tr>
          <th v-for="tableField in tableFields" :key="tableField.nameToShow">
            {{ tableField.nameToShow }}
          </th>
        </tr>
      </thead>

      <tbody id="tbody">
        <tr v-for="tableRow in tableData" :key="tableRow.name">
          <td v-for="(columnValue, name, index) in tableRow" :key="index">
            {{ columnValue }}
            <LibraryInputTextField v-if="tableFields[index].possibleValuesList != null" style="display: none" @input="onInput"
                                   v-bind:nameToShow="'new' + tableFields[index].nameToShow"
                                   v-bind:nameInBackend="tableFields[index].nameInBackend"
                                   v-bind:type="tableFields[index].type"
                                   v-bind:possibleValuesList="tableFields[index].possibleValuesList"
                                   v-bind:urlToFindMappedValue="tableFields[index].urlToFindMappedValue"/>
          </td>
          <td>
            <label>
              <input type="checkbox" class="filled-in"/>
              <span></span>
            </label>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import LibraryInputTextField from "./LibraryInputTextField";

export default {
  name: "LibraryEntityTable",
  props: [
    'tableFields',
    'tableData'
  ],
  components: {
    LibraryInputTextField
  },
  methods: {
    onInput (data) {
      this.inputValuesWithFieldNamesForUpdate.push(data)
      this.$emit('inputForUpdate', this.inputValuesWithFieldNamesForUpdate)
    }
  },
  data() {
    return {
      tbody: [],
      inputValuesWithFieldNamesForUpdate: []
    }
  }
}
</script>

<style scoped>

</style>