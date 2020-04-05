# Libraries
library(ggplot2)
library(matrixStats)
ta  <- as.matrix(read.table(file.choose(), sep=",", header = FALSE))
ta <- t(ta)
dim(ta)
head(ta)

# Create data frame
data <- data.frame(
  y=ta[,1],
  y2=ta[,2],
  y3=ta[,3],
  y4=ta[,4],
  y5=ta[,5],
  y6=ta[,6],
  y7=ta[,7],
  y8=ta[,8],
  y9=ta[,9],
  y10=ta[,10],
  y11=ta[,11],
  y12=ta[,12],
  y13=ta[,13],
  y14=ta[,14],
  y15=ta[,15],
  x=seq(1, length(ta[,1]))
)
ggplot(data, aes(x*1000)) +
  labs( x ="Zeitstempel", y = "Ø Belohnung pro Zeitstempel", color = "Lernrate") +
  ylim(-1.5,0.6) +
  geom_line(aes(y = y, colour = "0.1"), size=1)+
  geom_line(aes(y = y2, colour = "0.3"), size=1) +
  geom_line(aes(y = y3, colour = "0.5"), size=1) +
  geom_line(aes(y = y4, colour = "0.7"), size=1) +
  geom_line(aes(y = y5, colour = "0.9"), size=1) +
  theme_bw(base_size = 24)

ggplot(data, aes(x*1000)) +
  labs(x ="Zeitstempel", y = "Ø Belohnung pro Zeitstempel", color = "Lernrate") +
  ylim(-1.5,0.6) +
  geom_line(aes(y = y6, colour = "0.1"), size=1) +
  geom_line(aes(y = y7, colour = "0.3"), size=1) +
  geom_line(aes(y = y8, colour = "0.5"), size=1) +
  geom_line(aes(y = y9, colour = "0.7"), size=1) +
  geom_line(aes(y = y10, colour = "0.9"), size=1) +
  theme_bw(base_size = 24)

ggplot(data, aes(x*1000) ) +
  labs(x ="Zeitstempel", y = "Ø Belohnung pro Zeitstempel", color = "Lernrate") +
  ylim(-1.5,0.6) +
  geom_line(aes(y = y11, colour = "0.1"), size=1) +
  geom_line(aes(y = y12, colour = "0.3"), size=1) +
  geom_line(aes(y = y13, colour = "0.5"), size=1) +
  geom_line(aes(y = y14, colour = "0.7"), size=1) +
  geom_line(aes(y = y15, colour = "0.9"), size=1) +
  theme_bw(base_size = 24)