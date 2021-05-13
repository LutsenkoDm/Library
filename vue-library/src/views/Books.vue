<template>
  <div id="books">
    <LibraryEntityPage v-bind:table-fields="tableFields"
                       v-bind:table-data="tableData"
                       v-bind:entityName="'Book'"/>
  </div>

</template>

<script>
import LibraryEntityPage from "../components/LibraryEntityPage";

export default {
  name: 'Books',
  components: {
    LibraryEntityPage
  },
  data() {
    return {
      tableFields: [
        {nameToShow: 'Id',   nameInBackend: 'id' },
        {nameToShow: 'Name', nameInBackend: 'name',   type: "text"},
        {nameToShow: 'Cnt',  nameInBackend: 'cnt',    type: "number"},
        {nameToShow: 'Type', nameInBackend: 'typeId', type: "text", possibleValuesList: [], urlToFindMappedValue: '/bookTypeIdByName'},
      ],
      tableData: [],
    }
  },
  async mounted() {
    const booksResponse = await fetch('http://localhost:8081/lib/books', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (booksResponse.ok) {
      this.tableData = Array.from(await booksResponse.json());
    }

    const bookTypeNamesResponse = await fetch('http://localhost:8081/lib/bookTypeNames', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (bookTypeNamesResponse.ok) {
      this.tableFields[3].possibleValuesList = Array.from(await bookTypeNamesResponse.json());
    }
  }
}

</script>

<style scoped lang="scss">
@import '~materialize-css/dist/css/materialize.min.css';

</style>


