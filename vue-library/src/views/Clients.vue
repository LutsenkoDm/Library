<template>
  <div id="clients">
    <LibraryEntityPage v-bind:table-fields="tableFields"
                       v-bind:table-data="tableData"
                       v-bind:entityName="'Client'"/>
  </div>
</template>

<script>
import LibraryEntityPage from "../components/LibraryEntityPage";

export default {
  name: 'Clients',
  components: {
    LibraryEntityPage
  },
  data() {
    return {
      tableFields: [
        {nameToShow: 'Id',             nameInBackend: 'id' },
        {nameToShow: 'First name',     nameInBackend: 'firstName',     type: "text"},
        {nameToShow: 'Last name',      nameInBackend: 'lastName',      type: "text"},
        {nameToShow: 'Pather name',    nameInBackend: 'patherName',    type: "text"},
        {nameToShow: 'Passport seria', nameInBackend: 'passportSeria', type: "text"},
        {nameToShow: 'Passport num',   nameInBackend: 'passportNum',   type: "text"},
      ],
      tableData: []
    }
  },
  async mounted() {
    const clientsResponse = await fetch('http://localhost:8081/lib/clients', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (clientsResponse.ok) {
      this.tableData = Array.from(await clientsResponse.json());
    }
  }
}

</script>

<style scoped lang="scss">
@import '~materialize-css/dist/css/materialize.min.css';

</style>


