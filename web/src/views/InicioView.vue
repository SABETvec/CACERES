<template>
  <div class="VistaInicio">
    <div class="BienvenidaContenido">
      <div class="LineaDecorativa" />
      <p class="TextoSaludo">Bienvenido de regreso,</p>
      <h1 class="NombreBienvenida">
        {{ AlmacenSesion.DatosUsuario?.Nombre }}
        {{ AlmacenSesion.DatosUsuario?.ApellidoPaterno }}
      </h1>
      <p class="TextoSubtitulo">Sistema de Gestión — Librería Cáceres</p>
      <div class="LineaDecorativa" />
    </div>
  </div>
</template>

<script setup>
import { useSesionStore } from '@/stores/sesionStore'

const AlmacenSesion = useSesionStore()
</script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Crimson+Pro:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&display=swap');
@import '@/style/globals.css';
</style>
