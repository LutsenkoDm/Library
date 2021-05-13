<template>
  <div id="journal">
    <LibraryEntityPage v-bind:table-fields="tableFields" @reloadAvailableBookNames="loadAvailableBookNames" @reloadJournal="loadJournal"
                       v-bind:table-data="tableData"
                       v-bind:entityName="'JournalRecord'"/>
  </div>
</template>

<script>
import LibraryEntityPage from "../components/LibraryEntityPage";

export default {
  name: 'Journal',
  components: {
    LibraryEntityPage
  },
  data() {
    return {
      tableFields: [
        {nameToShow: 'Id',       nameInBackend: 'id' },
        {nameToShow: 'Book',     nameInBackend: 'bookId',   type: "text", possibleValuesList: [], urlToFindMappedValue: '/bookIdByName'},
        {nameToShow: 'Client',   nameInBackend: 'clientId', type: "text", possibleValuesList: [], urlToFindMappedValue: '/clientIdByFirstLastNameAndPassportNumber'},
        {nameToShow: 'Date beg', nameInBackend: 'dateBeg',  type: "date"},
        {nameToShow: 'Date end', nameInBackend: 'dateEnd',  type: "date"},
        {nameToShow: 'Date ret', nameInBackend: 'dateRet',  type: "date"},
      ],
      tableData: [],
    }
  },
  methods: {
    async loadAvailableBookNames() {
      const availableBookNamesResponse = await fetch('http://localhost:8081/lib/availableBookNames', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      if (availableBookNamesResponse.ok) {
        this.tableFields[1].possibleValuesList = Array.from(await availableBookNamesResponse.json());
      }
    },
    async loadClients() {
      const clientsResponse = await fetch('http://localhost:8081/lib/clients', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      if (clientsResponse.ok) {
        const clients = Array.from(await clientsResponse.json())
        const clientsInfoToShow = [clients.length]
        for (let i = 0; i < clients.length; i++) {
          let clientInfo = []
          clientInfo.push(clients[i].firstName)
          clientInfo.push(clients[i].lastName)
          clientInfo.push(clients[i].passportNum)
          clientsInfoToShow[i] = clientInfo
        }
        this.tableFields[2].possibleValuesList = clientsInfoToShow
      }
    },
    async loadJournal() {
      const journalResponse = await fetch('http://localhost:8081/lib/journal', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      console.log(journalResponse);
      if (journalResponse.ok) {
        this.tableData = Array.from(await journalResponse.json());
      }
    }
  },

  mounted: async function () {
    await this.loadJournal()
    await this.loadAvailableBookNames()
    await this.loadClients()
  }
}

</script>

<style scoped lang="scss">
@import '~materialize-css/dist/css/materialize.min.css';

</style>


