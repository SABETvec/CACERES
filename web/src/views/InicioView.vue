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
</style>
<style scoped>

.VistaInicio {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 60px);
}

.BienvenidaContenido {
  text-align: center;
  padding: 40px;
}

.LineaDecorativa {
  width: 60px;
  height: 1px;
  background: linear-gradient(to right, transparent, #c4912a, transparent);
  margin: 24px auto;
}

.TextoSaludo {
  font-family: 'Crimson Pro', serif;
  font-style: italic;
  font-size: 20px;
  color: #8a7d6b;
  margin-bottom: 8px;
}

.NombreBienvenida {
  font-family: 'Libre Baskerville', serif;
  font-size: 42px;
  font-weight: 700;
  color: #f5f0e8;
  margin-bottom: 12px;
}

.TextoSubtitulo {
  font-family: 'Crimson Pro', serif;
  font-size: 14px;
  color: #3d3320;
  letter-spacing: 2px;
  text-transform: uppercase;
}
</style>