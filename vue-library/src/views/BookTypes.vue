<template>
  <div id="bookTypes">
    <LibraryEntityPage v-bind:table-fields="tableFields"
                       v-bind:table-data="tableData"
                       v-bind:entityName="'BookType'"/>

  </div>
</template>

<script>
import LibraryEntityPage from "../components/LibraryEntityPage";

export default {
  name: 'BookTypes',
  components: {
    LibraryEntityPage
  },
  data() {
    return {
      tableFields: [
        {nameToShow: 'Id',        nameInBackend: 'id' },
        {nameToShow: 'Name',      nameInBackend: 'name', type: "text"},
        {nameToShow: 'Fine',      nameInBackend: 'fine', type: "number"},
        {nameToShow: 'Day count', nameInBackend: 'dayCount', type: "number"},
      ],
      tableData: [],
    }
  },
  async mounted() {
    const bookTypesResponse = await fetch('http://localhost:8081/lib/bookTypes', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (bookTypesResponse.ok) {
      this.tableData = Array.from(await bookTypesResponse.json());
    }
  }
}

</script>

<style scoped lang="scss">
@import '~materialize-css/dist/css/materialize.min.css';

</style>


