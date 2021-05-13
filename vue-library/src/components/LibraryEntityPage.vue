<template>
  <div id="journal">
    <LibraryNavbar/>

    <div class="row" style="margin-left: 9%; margin-top: 5%; margin-right: 1%">
      <div class="col s3">
        <form>
          <LibraryInputTextField v-for="field in tableFields" :key="field.nameToShow" @input="onInput"
                                 v-bind:nameToShow="field.nameToShow"
                                 v-bind:nameInBackend="field.nameInBackend"
                                 v-bind:type="field.type"
                                 v-bind:possibleValuesList="field.possibleValuesList"
                                 v-bind:urlToFindMappedValue="field.urlToFindMappedValue"
          />
          <div class="col s3">
            <button id="addButton" class="btn-large left" @click.prevent="add">Add</button>
            <button id="deleteButton" class="btn-large left" @click.prevent="deleteAllSelectedItems">Delete selected</button>
          </div>
        </form>
      </div>

      <div class="col s9">
        <div class="row">
          <LibrarySearchBlock style="margin-left: 12px" @search="onSearch"/>
          <button id="updateButton" class="btn left" v-on:click="update">Update selected</button>
          <button id="saveUpdateButton" class="btn left" v-on:click="saveUpdate" style="display: none">Save update</button>
        </div>
        <LibraryEntityTable v-bind:table-fields="tableFields" @input="onInput" @inputForUpdate="onInputForUpdate"
                            v-bind:table-data="filteredOnSearch">
        </LibraryEntityTable>
      </div>
    </div>
    <router-view/>
  </div>
</template>

<script>
import LibraryNavbar from "../components/LibraryNavbar";
import LibraryInputTextField from "../components/LibraryInputTextField";
import LibraryEntityTable from "../components/LibraryEntityTable";
import LibrarySearchBlock from "./LibrarySearchBlock";

export default {
  name: 'LibraryEntityPage',
  props: [
    'tableFields',
    'tableData',
    'entityName'
  ],
  components: {
    LibraryNavbar,
    LibraryInputTextField,
    LibraryEntityTable,
    LibrarySearchBlock,
  },
  computed: {
    filteredOnSearch() {
      return this.tableData.filter(data => JSON.stringify(data).includes(this.valueToSearch))
    }
  },
  methods: {
    onSearch (data) {
      this.valueToSearch = data.valueToSearch
    },
    onInput (data) {
      this.inputValuesWithFieldNames.push(data)
    },
    onInputForUpdate (data) {
      this.inputValuesWithFieldNamesForUpdate = data
    },
    async add () {
      let stringForJson = '{'
      for (let i = 0; i < this.inputValuesWithFieldNames.length; i++) {
        stringForJson += ('"' + this.inputValuesWithFieldNames[i].nameInBackend
                      + '":"' + this.inputValuesWithFieldNames[i].value + '",')
      }
      stringForJson = stringForJson.substring(0, stringForJson.length - 1) + '}'
      const addResponse = await fetch('http://localhost:8081/lib/add' + this.entityName, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: stringForJson
      });
      if (addResponse.ok) {
        console.log("add succesfull")
        this.tableData.push(await addResponse.json())
      } else {
        console.log("add fail")
        alert("add fail")
      }
    },
    update () {
      this.$emit('reloadAvailableBookNames')
      let trs = document.querySelector('#tbody').getElementsByTagName('tr')
      for (let i = 0; i < trs.length; i++) {
        let tds = trs[i].getElementsByTagName('td')
        if (tds[tds.length - 1].getElementsByTagName('input')[0].checked) {
          this.trsToUpdate.push(trs[i])
          for (let j = 1; j < tds.length - 1; j++) {
            let possibleInputValues = tds[j].getElementsByTagName('div')
            tds[j].style.boxShadow='0 0 6px 4px #eeff41'
            if (possibleInputValues.length === 0) {
              tds[j].setAttribute('contenteditable', 'true')
            } else {
              possibleInputValues[0].style.display='inline'
            }
          }
        }
      }
      document.querySelector('#updateButton').style.display='none'
      document.querySelector('#saveUpdateButton').style.display='inline'
    },
    async saveUpdate () {
      for (let i = 0; i < this.trsToUpdate.length; i++) {
        let tds = this.trsToUpdate[i].getElementsByTagName('td')
        let stringForJson = '{'
        let tdsWithPossibleValuesCounter = 0
        for (let j = 1; j < tds.length - 1; j++) {
          if (tds[j].getElementsByTagName('div').length === 0) {
            stringForJson += ('"' + this.tableFields[j].nameInBackend
                + '":"' + tds[j].textContent + '",')
          } else {
            if (this.inputValuesWithFieldNamesForUpdate[tdsWithPossibleValuesCounter] !== undefined) {
              stringForJson += ('"' + this.inputValuesWithFieldNamesForUpdate[tdsWithPossibleValuesCounter].nameInBackend
                  + '":"' + this.inputValuesWithFieldNamesForUpdate[tdsWithPossibleValuesCounter].value + '",')
              tdsWithPossibleValuesCounter = tdsWithPossibleValuesCounter + 1
            }
          }
        }
        stringForJson = stringForJson.substring(0, stringForJson.length - 1) + '}'
        const updateResponse = await fetch('http://localhost:8081/lib/'
            + this.entityNameWithFirstLetterInLowerCase + '/' + tds[0].textContent, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: stringForJson
        });
        if (updateResponse.ok) {
          console.log("update succesfull")
          tds.forEach(td => {
            td.style.boxShadow='none';
            let possibleInputValues = td.getElementsByTagName('div')
            if (possibleInputValues.length === 0) {
              td.setAttribute('contenteditable', 'false');
            } else {
              possibleInputValues[0].style.display='none'
            }
          })
          tds[tds.length - 1].getElementsByTagName('input')[0].checked = false
        } else {
          tds.forEach(td => {
            let possibleInputValues = td.getElementsByTagName('div')
            if (possibleInputValues.length === 0) {
              td.setAttribute('contenteditable', 'false');
            } else {
              possibleInputValues[0].style.display='none'
            }
          })
          console.log("update fail")
          alert("update fail")
        }
      }
      this.$emit('reloadAvailableBookNames')
      document.querySelector('#updateButton').style.display='inline'
      document.querySelector('#saveUpdateButton').style.display='none'
    },
    async deleteAllSelectedItems() {
      let entityIdsToDelete = []
      let trs = document.querySelector('#tbody').getElementsByTagName('tr')
      for (let i = 0; i < trs.length; i++) {
        let tds = trs[i].getElementsByTagName('td')
        let checkBoxes = tds[tds.length - 1].getElementsByTagName('input')
        for (let j = 0; j < checkBoxes.length; j++) {
          if (checkBoxes[j].checked) {
            entityIdsToDelete.push(tds[0].textContent)
            checkBoxes[j].checked = false
          }
        }
      }
      for (let entityIdToDelete of entityIdsToDelete) {
        const deleteResponse = await fetch('http://localhost:8081/lib/'
            + this.entityNameWithFirstLetterInLowerCase + '/' + entityIdToDelete, {
          method: 'DELETE'
        });
        if (deleteResponse.ok) {
          console.log("delete succesfull")
          for (let item of this.tableData) {
            if (item.id == entityIdToDelete) {
              this.tableData.splice(this.tableData.indexOf(item), 1)
              break
            }
          }
        } else {
          console.log("delete fail")
          alert("delete fail")
        }
      }
    }
  },
  data() {
    return {
      entityNameWithFirstLetterInLowerCase: (this.entityName[0].toLowerCase() + this.entityName.substring(1, this.entityName.length)),
      valueToSearch: '',
      inputValuesWithFieldNames: [],
      inputValuesWithFieldNamesForUpdate: [],
      trsToUpdate: []
    }
  }
}

</script>

<style scoped lang="scss">
@import '~materialize-css/dist/css/materialize.min.css';

#journal {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#addButton {
  margin-top: 25px;
  margin-left: -12px;
  white-space: nowrap;
}

#deleteButton {
  margin-top: 25px;
  margin-left: -12px;
  white-space: nowrap;
}

#updateButton {
  margin-top: 2%;
  margin-left: 4%;
}

#saveUpdateButton {
  margin-top: 2%;
  margin-left: 4%;
}

</style>


