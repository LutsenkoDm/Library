<template>
  <nav class="nav-extended">
    <div class="nav-wrapper teal lighten-2">
      <a class="brand-logo center" href="#">Library</a>
    </div>
    <div class="nav-content">
      <ul id="navTabs" class="tabs tabs-transparent">
        <li class="tab"><router-link to="/journal">Journal</router-link></li>
        <li class="tab"><router-link to="/clients">Clients</router-link></li>
        <li class="tab"><router-link to="/books">Books</router-link></li>
        <li class="tab"><router-link to="/bookTypes">Books types</router-link></li>
      </ul>
    </div>
  </nav>
</template>

<script>
export default {
  name: "LibraryNavbar"
}
</script>

<style scoped>
  #navTabs {
    display: flex;
    justify-content: space-around;
  }
</style>