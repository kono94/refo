# Libraries
library(ggplot2)
library(matrixStats)
ta  <- as.matrix(read.table(file.choose(), sep=",", header = FALSE, skip = 1))
ta <- t(ta)
dim(ta)
head(ta)

# Create data frame
data <- data.frame(
  y=ta[1:20,1],
  y2=ta[1:20,2],
  y3=ta[1:20,3],
  y4=ta[1:20,4],
  y5=ta[1:20,5],
  y6=ta[1:20,6],
  y7=ta[1:20,7],
  y8=ta[1:20,8],
  x=seq(1, length(ta[1:20,1]))
)
ggplot(data, aes(x*1000)) +
  labs( x ="Gesamtanzahl gesammeltes Futter", y = "Ø Zeitstempel pro Futter (log)", color = "Diskontierungsfaktor") +
  #scale_y_log10()+
  geom_hline(yintercept=23, linetype="dashed")+
  geom_text(aes(0,23,label = "opt", vjust = -1)) +
  geom_line(aes(y = y, colour = "0.05"), size=1)+
  geom_line(aes(y = y2, colour = "0.1"), size=1) +
  geom_line(aes(y = y3, colour = "0.3"), size=1) +
  geom_line(aes(y = y4, colour = "0.5"), size=1) +
  geom_line(aes(y = y5, colour = "0.7"), size=1)+
  geom_line(aes(y = y6, colour = "0.9"), size=1) +
  geom_line(aes(y = y7, colour = "0.95"), size=1) +
  geom_line(aes(y = y8, colour = "0.99"), size=1) +
  theme_bw(base_size = 24)
